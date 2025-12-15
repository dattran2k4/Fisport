package com.fisport.service.impl;

import com.fisport.dto.request.LoginRequest;
import com.fisport.dto.request.RegisterRequestDTO;
import com.fisport.dto.request.ResetPasswordRequest;
import com.fisport.dto.request.TwoFARequest;
import com.fisport.dto.response.LoginResponse;
import com.fisport.dto.response.RegisterResponseDTO;
import com.fisport.dto.response.TokenResponse;
import com.fisport.exception.InvalidDataException;
import com.fisport.exception.ResourceNotFoundException;
import com.fisport.model.Role;
import com.fisport.model.User;
import com.fisport.model.Wallet;
import com.fisport.repository.RoleRepository;
import com.fisport.repository.UserRepository;
import com.fisport.repository.WalletRepository;
import com.fisport.security.CustomUserDetails;
import com.fisport.service.*;
import com.fisport.common.ERole;
import com.fisport.common.EUserStatus;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;


@RequiredArgsConstructor
@Service
@Slf4j(topic = "AUTH-SERVICE")
public class AuthServiceImpl implements AuthService {

    @Value("${endpoint.confirmUser}")
    private String endPointConfirmUser;

    @Value("${endpoint.endPointResetPassword}")
    private String endPointResetPassword;

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final AuthenticationManager authenticationManager;
    private final MailService mailService;
    private final CaffeineTokenService tokenService;
    private final TwoFAService twoFAService;
    private final SessionService sessionService;
    private final SecurityContextService securityContextService;
    private final WalletRepository walletRepository;
    private final JwtService jwtService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RegisterResponseDTO register(RegisterRequestDTO registerRequestDTO) {
        if (userRepository.findByUsername(registerRequestDTO.getUsername()).isPresent()) {
            throw new InvalidDataException("tài khoản đã tồn tại");
        }
        if (userRepository.findByEmail(registerRequestDTO.getEmail()).isPresent()) {
            throw new InvalidDataException("email đã tồn tại");
        }

        if (!registerRequestDTO.getPassword().equals(registerRequestDTO.getConfirmPassword())) {
            throw new InvalidDataException("Mật khẩu không trùng với mật khẩu xác nhận");
        }

        if (userRepository.findByPhone(registerRequestDTO.getPhone()).isPresent()) {
            throw new InvalidDataException("Số điện thoại đã tồn tại");
        }

        Role role = roleRepository.findByName(ERole.USER)
                .orElseThrow(() -> new ResourceNotFoundException("Default role 'USER' not found"));

        User user = User.builder()
                .username(registerRequestDTO.getUsername())
                .email(registerRequestDTO.getEmail())
                .password(passwordEncoder.encode(registerRequestDTO.getPassword()))
                .birthday(registerRequestDTO.getBirthday())
                .gender(registerRequestDTO.getGender())
                .phone(registerRequestDTO.getPhone())
                .status(EUserStatus.INACTIVE)
                .role(role)
                .build();

        User savedUser = userRepository.save(user);

        //add wallet
        Wallet wallet = new Wallet();
        wallet.setUser(savedUser);

        Wallet savedWallet = walletRepository.save(wallet);

        savedUser.setWallet(savedWallet);

        userRepository.save(savedUser);

        log.info("User id {} registered",  user.getId());

        //Create token
        String verifyCode = tokenService.createTokenForEmail(registerRequestDTO.getEmail());

        mailService.sendConfirmLink(user.getEmail(), "confirm-email.html", endPointConfirmUser, verifyCode);

        return RegisterResponseDTO.builder()
                .username(user.getUsername())
                .build();
    }

    @Override
    public LoginResponse login(LoginRequest request) {


        User user = userRepository.findByUsername(request.getUsername()).orElseThrow();

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new ResourceNotFoundException("Sai mật khẩu");
        }

        if (user.isTwoFAEnable()) {
            sessionService.set("PRE_AUTH_USER", request.getUsername());
            return LoginResponse.builder()
                    .username(user.getUsername())
                    .email(user.getEmail())
                    .is2FAEnabled(true)
                    .role(String.valueOf(user.getRole().getName()))
                    .build();
        }

        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );
        securityContextService.save(auth);
//        new ChangeSessionIdAuthenticationStrategy().onAuthentication(authentication, httpRequest, httpResponse);
        return LoginResponse.builder()
                .username(user.getUsername())
                .is2FAEnabled(false)
                .role(String.valueOf(user.getRole().getName()))
                .build();
    }

    @Override
    public boolean verify2FA(TwoFARequest request) {
        String preAuthUser = sessionService.get("PRE_AUTH_USER", String.class);
        if (preAuthUser == null || !preAuthUser.equals(request.getUsername())) {
//            throw new RuntimeException("Session không hợp lệ hoặc đã hết hạn");
            return false;
        }

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy user"));

        int code = Integer.parseInt(request.getCode());

        if (!twoFAService.verifyCode(user.getTwoFASecret(), code)) {
//            throw new ResourceNotFoundException("Mã 2FA không hợp lệ");
            return false;
        }

        CustomUserDetails userDetails = new CustomUserDetails(user);

        Authentication auth = new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities());
        securityContextService.save(auth);
        sessionService.remove("PRE_AUTH_USER");
        return true;
    }

    @Override
    public void verify2FARegister(String username, String code) {
        int c = Integer.parseInt(code);
        User user = userRepository.findByUsername(username).orElse(null);
        if(twoFAService.verifyCode(user.getTwoFASecret(), c)) {
            user.setStatus(EUserStatus.ACTIVE);

            log.info("UserId {} registered 2FA",  user.getId());
        } else {
            throw new InvalidDataException("Nhập sai , vui lòng nhập lại");
        }
        userRepository.save(user);
    }


    @Override
    public String confirmUser(String verifyCode) {
        //Check email
        String email = tokenService.getEmailByToken(verifyCode).orElseThrow(() -> new ResourceNotFoundException("Token không hợp lệ"));

        User user = userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy user"));

        //Remove token if user active
        if (user.getStatus().equals(EUserStatus.ACTIVE)) {
            tokenService.invalidateToken(verifyCode);
            return "Tài khoản đã được kích hoạt trước đó.";
        }

        //Set secret from GGAuthentication
        String secret = twoFAService.generateSecret();
        user.setTwoFAEnable(true);
        user.setTwoFASecret(secret);


        String qrURL = twoFAService.getOtpAuthURL(user.getUsername(), secret);

        userRepository.save(user);

        tokenService.invalidateToken(verifyCode);
        return qrURL;
    }

    @Override
    public String logout() {
        securityContextService.clear();

        return "Đăng xuất thành công!";
    }

    @Override
    public void forgotPassword(String email) throws MessagingException, UnsupportedEncodingException {
        if (!email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) {
            throw new InvalidDataException("Email sai định dạng");
        }

        User user = userRepository.findByEmail(email).orElseThrow(() -> new InvalidDataException("Email không tồn tại trong hệ thống"));

        if (user != null) {
            //to-fix
            String token = tokenService.createTokenForEmail(user.getEmail());
            mailService.sendConfirmLink(email, "reset-password-email.html", endPointResetPassword, token);
        }

    }

    @Override
    public void resetPassword(ResetPasswordRequest request, String verifyCode) {
        //user không phải email
        //String email = tokenService.getEmailByToken(verifyCode).orElseThrow(() -> new RuntimeException("Token không hợp lệ hoặc đã hết hạn"));

        String username = tokenService.getEmailByToken(verifyCode).orElseThrow(() -> new RuntimeException("Token không hợp lệ hoặc đã hết hạn"));

        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("Người dùng không tồn tại"));

        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new InvalidDataException("Mật khẩu xác nhận không khớp");
        }

        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);

        tokenService.invalidateToken(verifyCode);
    }

    @Override
    public String getRoleByUserName(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException("Not found"));
        return user.getRole().getName().toString();
    }

    @Override
    public TokenResponse authenticate(LoginRequest request) {
        log.info("---------- authenticate ----------");

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getUsername(),
                request.getPassword()
                ));

        var userEntity = userRepository.findByUsername(request.getUsername()).orElseThrow(() -> new ResourceNotFoundException("User Not found"));

        var userDetails = new CustomUserDetails(userEntity);

        String accessToken = jwtService.generateAccessToken(userDetails);

        String refreshToken = jwtService.generateRefreshToken(userDetails);

        return TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();

    }

}



