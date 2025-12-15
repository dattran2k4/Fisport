package com.fisport.service;

import com.fisport.common.TokenType;
import com.fisport.security.CustomUserDetails;
import org.springframework.security.core.userdetails.UserDetails;

public interface JwtService {

    String extractUsername(String token, TokenType type);

    String generateAccessToken(CustomUserDetails userDetails);

    String generateRefreshToken(CustomUserDetails userDetails);

    boolean isTokenValid(String token, TokenType type, UserDetails userDetails);
}
