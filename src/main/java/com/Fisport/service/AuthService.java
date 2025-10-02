package com.Fisport.service;

import com.Fisport.dto.request.LoginRequestDTO;
import com.Fisport.dto.request.RegisterRequestDTO;
import com.Fisport.dto.response.LoginResponse;
import com.Fisport.dto.response.LoginResponseDTO;
import com.Fisport.dto.response.RegisterResponseDTO;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.UnsupportedEncodingException;

public interface AuthService {
    LoginResponseDTO login(LoginRequestDTO loginRequestDTO);
    RegisterResponseDTO register(RegisterRequestDTO registerRequestDTO) throws MessagingException, UnsupportedEncodingException;
    LoginResponse loginApi(LoginRequestDTO request, HttpSession session);

    String confirmUser(Long userId, String verifyCode);
}
