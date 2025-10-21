package com.Fisport.service.impl;

import com.Fisport.dto.request.LoginRequestDTO;
import com.Fisport.dto.request.RegisterRequestDTO;
import com.Fisport.dto.request.ResetPasswordRequest;
import com.Fisport.dto.request.TwoFARequest;
import com.Fisport.dto.response.LoginResponse;
import com.Fisport.dto.response.RegisterResponseDTO;
import com.Fisport.exception.InvalidDataException;
import com.Fisport.exception.ResourceNotFoundException;
import com.Fisport.model.Role;
import com.Fisport.model.User;
import com.Fisport.repository.RoleRepository;
import com.Fisport.repository.UserRepository;
import com.Fisport.security.CustomUserDetails;
import com.Fisport.service.*;
import com.Fisport.common.ERole;
import com.Fisport.common.EUserStatus;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RequiredArgsConstructor
@Service
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

    @Override
    public RegisterResponseDTO register(RegisterRequestDTO registerRequestDTO) throws MessagingException, UnsupportedEncodingException {
        if (userRepository.findByUsername(registerRequestDTO.getUsername()).isPresent()) {
            throw new InvalidDataException("tài khoản đã tồn tại");
        }
        if (userRepository.findByEmail(registerRequestDTO.getEmail()).isPresent()) {
            throw new InvalidDataException("email đã tồn tại");
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

        userRepository.save(user);

        //Create token
        String verifyCode = tokenService.createTokenForEmail(registerRequestDTO.getEmail());

        mailService.sendConfirmLink(user.getEmail(), "confirm-email.html", endPointConfirmUser, verifyCode);

        return RegisterResponseDTO.builder()
                .username(user.getUsername())
                .build();
    }

    @Override
    public LoginResponse login(LoginRequestDTO request) {


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
        twoFAService.verifyCode(user.getTwoFASecret(), c);
        user.setStatus(EUserStatus.ACTIVE);
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
            throw new RuntimeException("Mật khẩu xác nhận không khớp");
        }

        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);

        tokenService.invalidateToken(verifyCode);
    }

    @Override
    public String getRoleByUserName(String username) {
        User user = userRepository.findByUsername(username).orElse(null);
        return user.getRole().getName().toString();
    }

}



