package com.Fisport.service;

import com.Fisport.dto.request.LoginRequestDTO;
import com.Fisport.dto.request.RegisterRequestDTO;
import com.Fisport.dto.response.LoginResponseDTO;
import com.Fisport.dto.response.RegisterResponseDTO;

public interface AuthService {
    LoginResponseDTO login(LoginRequestDTO loginRequestDTO);
    RegisterResponseDTO register(RegisterRequestDTO registerRequestDTO);
}
