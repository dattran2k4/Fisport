package com.fisport.service;

import com.fisport.model.User;
import com.fisport.security.CustomUserDetails;
import com.fisport.service.impl.SessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SecurityContextService {

    private final SessionService sessionService;

    public void save(Authentication authentication) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);

        sessionService.set(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, context);
    }

    public void clear() {
        SecurityContextHolder.clearContext();
        sessionService.clear();
    }

    /** Cập nhật principal trong session sau khi user thay đổi thông tin */
    public void updateAuthentication(User user) {
        Authentication currentAuth = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails updatedPrincipal = new CustomUserDetails(user);
        Authentication newAuth = new UsernamePasswordAuthenticationToken(
                updatedPrincipal,
                currentAuth.getCredentials(),
                updatedPrincipal.getAuthorities()
        );
        SecurityContextHolder.getContext().setAuthentication(newAuth);
    }
}
