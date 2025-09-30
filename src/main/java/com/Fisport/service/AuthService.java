package com.Fisport.service;

import com.Fisport.dto.request.LoginRequestDTO;
import com.Fisport.dto.request.RegisterRequestDTO;
import com.Fisport.dto.response.LoginResponse;
import com.Fisport.dto.response.LoginResponseDTO;
import com.Fisport.dto.response.RegisterResponseDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthService {
    LoginResponseDTO login(LoginRequestDTO loginRequestDTO);
    RegisterResponseDTO register(RegisterRequestDTO registerRequestDTO);
    LoginResponse loginApi(LoginRequestDTO request, HttpServletRequest httpRequest,  HttpServletResponse httpResponse);
}
