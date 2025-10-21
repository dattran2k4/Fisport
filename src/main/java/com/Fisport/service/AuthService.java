package com.Fisport.service;

import com.Fisport.dto.request.LoginRequestDTO;
import com.Fisport.dto.request.RegisterRequestDTO;
import com.Fisport.dto.request.ResetPasswordRequest;
import com.Fisport.dto.request.TwoFARequest;
import com.Fisport.dto.response.LoginResponse;
import com.Fisport.dto.response.RegisterResponseDTO;
import com.Fisport.dto.response.TwoFAResponse;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpSession;

import java.io.UnsupportedEncodingException;

public interface AuthService {
    RegisterResponseDTO register(RegisterRequestDTO registerRequestDTO) throws MessagingException, UnsupportedEncodingException;

    LoginResponse login(LoginRequestDTO request);

    boolean verify2FA(TwoFARequest request);

    void verify2FARegister(String username, String code);

    String confirmUser(String verifyCode);

    String logout();

    void forgotPassword(String email) throws MessagingException, UnsupportedEncodingException;

    void resetPassword(ResetPasswordRequest request, String verifyCode);

    String getRoleByUserName(String username);

}
