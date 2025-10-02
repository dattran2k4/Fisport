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

    private static final List<String> ALLOWED_PATHS = List.of(
            "/api/auth/login",
            "/api/auth/register",
            "/api/auth/confirm",
            "/api/auth/2fa/verify"
    );

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        String path = request.getRequestURI();

        if (ALLOWED_PATHS.stream().anyMatch(path::startsWith)) {
            filterChain.doFilter(request, response);
            return;
        }

        if (session == null) {
            writeJsonError(response, HttpServletResponse.SC_UNAUTHORIZED, "Chưa đăng nhập");
            return;
        }

        if (session.getAttribute("PRE_AUTH_USER") != null) {
            writeJsonError(response, HttpServletResponse.SC_UNAUTHORIZED, "Bạn phải xác thực 2FA trước khi truy cập API khác");
            return;
        }

        // Nếu có full SecurityContext → cho qua
        if (session.getAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY) != null) {
            filterChain.doFilter(request, response);
            return;
        }

        // Trường hợp khác (session có nhưng không hợp lệ)
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        writeJsonError(response, HttpServletResponse.SC_UNAUTHORIZED, "Session không hợp lệ");
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
