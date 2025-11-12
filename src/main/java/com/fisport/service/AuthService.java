package com.fisport.service;

import com.fisport.dto.request.LoginRequestDTO;
import com.fisport.dto.request.RegisterRequestDTO;
import com.fisport.dto.request.ResetPasswordRequest;
import com.fisport.dto.request.TwoFARequest;
import com.fisport.dto.response.LoginResponse;
import com.fisport.dto.response.RegisterResponseDTO;
import jakarta.mail.MessagingException;

import java.io.UnsupportedEncodingException;

public interface AuthService {
    RegisterResponseDTO register(RegisterRequestDTO registerRequestDTO);

    LoginResponse login(LoginRequestDTO request);

    boolean verify2FA(TwoFARequest request);

    void verify2FARegister(String username, String code);

    String confirmUser(String verifyCode);

    String logout();

    void forgotPassword(String email) throws MessagingException, UnsupportedEncodingException;

    void resetPassword(ResetPasswordRequest request, String verifyCode);

    String getRoleByUserName(String username);

}
