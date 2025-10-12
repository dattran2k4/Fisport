package com.Fisport.service;

import com.Fisport.dto.request.LoginRequestDTO;
import com.Fisport.dto.request.RegisterRequestDTO;
import com.Fisport.dto.request.ResetPasswordRequest;
import com.Fisport.dto.request.TwoFARequest;
import com.Fisport.dto.response.LoginResponse;
import com.Fisport.dto.response.RegisterResponseDTO;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpSession;

import java.io.UnsupportedEncodingException;

public interface AuthService {
    RegisterResponseDTO register(RegisterRequestDTO registerRequestDTO) throws MessagingException, UnsupportedEncodingException;
    LoginResponse login(LoginRequestDTO request, HttpSession session);

    String confirmUser(String verifyCode);
    boolean verify2FA(TwoFARequest request, HttpSession session);
    String logout(HttpSession session);
    String forgotPassword(String email) throws MessagingException, UnsupportedEncodingException;
    void resetPassword(ResetPasswordRequest request, String verifyCode);
}
