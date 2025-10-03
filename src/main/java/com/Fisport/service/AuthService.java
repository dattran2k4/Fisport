package com.Fisport.service;

import com.Fisport.dto.request.LoginRequestDTO;
import com.Fisport.dto.request.RegisterRequestDTO;
import com.Fisport.dto.request.TwoFARequest;
import com.Fisport.dto.response.LoginResponse;
import com.Fisport.dto.response.RegisterResponseDTO;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpSession;

import java.io.UnsupportedEncodingException;

public interface AuthService {
    RegisterResponseDTO register(RegisterRequestDTO registerRequestDTO) throws MessagingException, UnsupportedEncodingException;
    LoginResponse loginApi(LoginRequestDTO request, HttpSession session);

    String confirmUser(Long userId, String verifyCode);
    LoginResponse verify2FA(TwoFARequest request, HttpSession session);
}
