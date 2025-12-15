package com.fisport.service.impl;

import com.fisport.common.TokenType;
import com.fisport.exception.InvalidDataException;
import com.fisport.security.CustomUserDetails;
import com.fisport.service.JwtService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "JWT-SERVICE")
public class JwtServiceImpl implements JwtService {

    @Value("${jwt.access-key}")
    private String accessKey;

    @Value("${jwt.refresh-key}")
    private String refreshKey;

    @Value("${jwt.expiration}")
    private long expiration;


    @Override
    public String extractUsername(String token, TokenType type) {
        return extractClaim(token, type, Claims::getSubject);
    }

    private <T> T extractClaim(String token, TokenType type, Function<Claims, T> claimsResolvers) {
        final Claims claims = extractAllClaims(token, type);
        return claimsResolvers.apply(claims);
    }

    private Claims extractAllClaims(String token, TokenType tokenType) {
        try {
            return Jwts.parserBuilder().setSigningKey(getSigningKey(tokenType)).build().parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            throw new AccessDeniedException("Token đã hết hạn. Vui lòng đăng nhập lại.");
        } catch (SignatureException e) {
            throw new AccessDeniedException("Token không hợp lệ hoặc đã bị thay đổi.");
        } catch (MalformedJwtException e) {
            throw new AccessDeniedException("Token không đúng định dạng.");
        } catch (UnsupportedJwtException e) {
            throw new AccessDeniedException("Token không được hỗ trợ.");
        } catch (IllegalArgumentException e) {
            throw new AccessDeniedException("Token không được cung cấp.");
        }
    }

    @Override
    public String generateAccessToken(CustomUserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userDetails.getUser().getId());
        claims.put("role", userDetails.getAuthorities());

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 5000))
                .signWith(getSigningKey(TokenType.ACCESS_TOKEN), SignatureAlgorithm.HS256).compact();
    }

    @Override
    public String generateRefreshToken(CustomUserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userDetails.getUser().getId());
        claims.put("role", userDetails.getAuthorities());

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24))
                .signWith(getSigningKey(TokenType.REFRESH_TOKEN), SignatureAlgorithm.HS256).compact();
    }

    @Override
    public boolean isTokenValid(String token, TokenType type, UserDetails userDetails) {
        final String username = extractUsername(token, type);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token, type));
    }

    private Key getSigningKey(TokenType type) {
        switch (type) {
            case ACCESS_TOKEN -> {
                return Keys.hmacShaKeyFor(accessKey.getBytes());
            }
            case REFRESH_TOKEN -> {
                return Keys.hmacShaKeyFor(refreshKey.getBytes());
            }
            default -> throw new InvalidDataException("Token type not found");
        }
    }

    private boolean isTokenExpired(String token, TokenType type) {
        return extractExpiration(token, type).before(new Date());
    }

    private Date extractExpiration(String token, TokenType type) {
        return extractClaim(token, type, Claims::getExpiration);
    }
}
