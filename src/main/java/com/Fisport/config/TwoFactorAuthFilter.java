package com.Fisport.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class TwoFactorAuthFilter extends OncePerRequestFilter {

    private static final List<String> WHITELIST = List.of(
            "/api/auth/**", "/api/v1/fields/**", "/common/**", "/api/v1/sub-fields/**", "/api/v1/field-types/**", "/web/**", "/css/**", "/img/**", "/favicon.ico"
    );

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        HttpSession session = request.getSession(false);

        if (session == null) {
            filterChain.doFilter(request, response);
            return;
        }

        String path = request.getRequestURI();
        if (WHITELIST.stream().anyMatch(path::startsWith)) {
            filterChain.doFilter(request, response);
            return;
        }

        if (session.getAttribute("PRE_AUTH_USER") != null && session.getAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY) == null) {
            filterChain.doFilter(request, response);
//            writeJsonError(response, HttpServletResponse.SC_UNAUTHORIZED, "Bạn phải xác thực 2FA trước khi truy cập API khác");
            return;
        }

        filterChain.doFilter(request, response);
    }

    private void writeJsonError(HttpServletResponse response, int status, String message) throws IOException {
        response.setContentType("application/json");
        response.setStatus(status);
        response.getWriter().write(
                String.format("{\"status\":%d,\"error\":\"%s\",\"message\":\"%s\"}",
                        status, HttpStatus.valueOf(status).name(), message)
        );
    }
}
