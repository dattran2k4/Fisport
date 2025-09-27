package com.Fisport.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Component
public class CustomAuthenticationFailureHandler  extends SimpleUrlAuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        String message = "Tài khoản hoặc mật khẩu không chính xác";

        if (exception instanceof DisabledException) {
            message = "Tài khoản chưa kích hoạt";
        } else if (exception instanceof LockedException) {
            message = "Tài khoản bị khoá";
        } // có thể mở rộng

        message = URLEncoder.encode(message, StandardCharsets.UTF_8);
        // redirect đến /login?error=true&message=...
        getRedirectStrategy().sendRedirect(request, response, "/login?error=true&message=" + message);
    }
}
