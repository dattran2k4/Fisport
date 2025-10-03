package com.Fisport.service.impl;

import com.Fisport.dto.request.LoginRequestDTO;
import com.Fisport.dto.request.RegisterRequestDTO;
import com.Fisport.dto.request.TwoFARequest;
import com.Fisport.dto.response.LoginResponse;
import com.Fisport.dto.response.LoginResponseDTO;
import com.Fisport.dto.response.RegisterResponseDTO;
import com.Fisport.exception.InvalidDataException;
import com.Fisport.exception.ResourceNotFoundException;
import com.Fisport.model.Role;
import com.Fisport.model.User;
import com.Fisport.repository.RoleRepository;
import com.Fisport.repository.UserRepository;
import com.Fisport.security.CustomUserDetails;
import com.Fisport.service.AuthService;
import com.Fisport.service.CaffeineTokenService;
import com.Fisport.service.MailService;
import com.Fisport.service.TwoFAService;
import com.Fisport.util.ERole;
import com.Fisport.util.EUserStatus;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;


@RequiredArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final AuthenticationManager authenticationManager;
    private final MailService mailService;
    private final CaffeineTokenService tokenService;
    private final TwoFAService  twoFAService;

    @Override
    public RegisterResponseDTO register(RegisterRequestDTO  registerRequestDTO) throws MessagingException, UnsupportedEncodingException {
        if (userRepository.findByUsername(registerRequestDTO.getUsername()).isPresent()) {
            throw new InvalidDataException("tài khoản đã tồn tại");
        }
        if (userRepository.findByEmail(registerRequestDTO.getEmail()).isPresent()) {
            throw new InvalidDataException("email đã tồn tại");
        }

        if (userRepository.findByPhone(registerRequestDTO.getPhone()).isPresent()) {
            throw new InvalidDataException("số điện thoại đã tồn tại");
        }

        Role role = roleRepository.findByName(ERole.USER)
                .orElseThrow(() -> new ResourceNotFoundException("Default role 'USER' not found"));

        User user = User.builder()
                .username(registerRequestDTO.getUsername())
                .email(registerRequestDTO.getEmail())
                .password(passwordEncoder.encode(registerRequestDTO.getPassword()))
                .phone(registerRequestDTO.getPhone())
                .birthday(registerRequestDTO.getBirthday())
                .gender(registerRequestDTO.getGender())
                .status(EUserStatus.INACTIVE)
                .role(role)
                .build();

        userRepository.save(user);

        //Create token
        String verifyCode = tokenService.createTokenForEmail(registerRequestDTO.getEmail());

        mailService.sendConfirmLink(user.getEmail(), user.getId(), verifyCode);

        return RegisterResponseDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .phone(user.getPhone())
                .birthday(user.getBirthday())
                .gender(user.getGender())
                .status(EUserStatus.ACTIVE)
                .roleName(String.valueOf(user.getRole().getName()))
                .build();
    }

    @Override
    public LoginResponse loginApi(LoginRequestDTO request, HttpSession session) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        // Lấy thông tin user
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        User user = userDetails.getUser();

        if (user.isTwoFAEnable()) {
            session.setAttribute("PRE_AUTH_USER", request.getUsername());
            return LoginResponse.builder()
                    .username(user.getUsername())
                    .email(user.getEmail())
                    .is2FAEnabled(true)
                    .message("Vui lòng nhập mã 2FA từ Google Authenticator")
                    .build();
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);
        session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                SecurityContextHolder.getContext());

//        new ChangeSessionIdAuthenticationStrategy().onAuthentication(authentication, httpRequest, httpResponse);


        String role = userDetails.getAuthorities().stream()
                .findFirst()
                .map(GrantedAuthority::getAuthority)
                .orElse(null);

        return LoginResponse.builder()
                .userId(userDetails.getUser().getId())
                .username(userDetails.getUsername())
                .birthDate(userDetails.getUser().getBirthday())
                .phoneNumber(userDetails.getUser().getPhone())
                .email(userDetails.getUser().getEmail())
                .gender(String.valueOf(userDetails.getUser().getGender()))
                .role(role)
                .is2FAEnabled(false)
                .message("Đăng nhập thành công")
                .build();
    }

    @Override
    public LoginResponse verify2FA(TwoFARequest request, HttpSession session) {
        String preAuthUser = (String) session.getAttribute("PRE_AUTH_USER");
        if (preAuthUser == null || !preAuthUser.equals(request.getUsername())) {
            throw new RuntimeException("Session không hợp lệ hoặc đã hết hạn");
        }

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy user"));

        if (!twoFAService.verifyCode(user.getTwoFASecret(), request.getCode())) {
            throw new ResourceNotFoundException("Mã 2FA không hợp lệ");
        }

        CustomUserDetails userDetails = new CustomUserDetails(user);
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities()
        );
        session.removeAttribute("PRE_AUTH_USER");
        SecurityContextHolder.getContext().setAuthentication(authentication);
        session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                SecurityContextHolder.getContext());

        return LoginResponse.builder()
                .userId(userDetails.getUser().getId())
                .username(userDetails.getUsername())
                .birthDate(userDetails.getUser().getBirthday())
                .phoneNumber(userDetails.getUser().getPhone())
                .email(userDetails.getUser().getEmail())
                .gender(String.valueOf(userDetails.getUser().getGender()))
                .role(userDetails.getAuthorities().stream()
                        .findFirst()
                        .map(GrantedAuthority::getAuthority)
                        .orElse(null))
                .message("Đăng nhập thành công")
                .build();
    }

    @Override
    public String confirmUser(Long userId, String verifyCode) {
        //Check email
        String email = tokenService.getEmailByToken(verifyCode).orElseThrow(() -> new ResourceNotFoundException("Token không hợp lệ"));

        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy user"));

        //Remove token if user active
        if (user.getStatus().equals(EUserStatus.ACTIVE)) {
            tokenService.invalidateToken(verifyCode);
            return "Tài khoản đã được kích hoạt trước đó.";
        }

        user.setStatus(EUserStatus.ACTIVE);

        //Set secret from GGAuthentication
        String secret = twoFAService.generateSecret();
        user.setTwoFAEnable(true);
        user.setTwoFASecret(secret);

        String qrURL = twoFAService.getOtpAuthURL(user.getUsername(), secret);

        userRepository.save(user);

        tokenService.invalidateToken(verifyCode);
        return String.format("Kích hoạt tài khoản thành công. Sử dụng ứng dụng Google Authentication để kích hoạt bảo vệ tài khoản: QR Code 2FA: %s", qrURL);
    }

}



