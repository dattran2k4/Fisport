package com.Fisport.service;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Optional;
import java.util.UUID;

@Service
public class CaffeineTokenService {

    private final Cache<String, Object> tokenCache;

    public CaffeineTokenService(@Value("${app.token.expiry-minutes}") long expiryMinutes) {
        this.tokenCache = Caffeine.newBuilder()
                .expireAfterWrite(Duration.ofMinutes(expiryMinutes))
                .maximumSize(100_000)
                .build();
    }

    public String createTokenForEmail(String email) {
        String token = UUID.randomUUID().toString();
        tokenCache.put(token, email);
        return token;
    }

    public Optional<String> getEmailByToken(String token) {
        return Optional.ofNullable(tokenCache.getIfPresent(token).toString());
    }

    public void invalidateToken(String token) {
        tokenCache.invalidate(token);
    }
}
