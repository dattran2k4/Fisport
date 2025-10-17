package com.Fisport.api;

import com.Fisport.dto.request.ForgotPasswordRequest;
import com.Fisport.dto.request.LoginRequestDTO;
import com.Fisport.dto.request.RegisterRequestDTO;
import com.Fisport.dto.request.ResetPasswordRequest;
import com.Fisport.dto.response.*;
import com.Fisport.service.AuthService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@Validated
@RequestMapping("/api/auth")
public class AuthApiController {
    private final AuthService authService;

    @PostMapping("/login")
    public ApiResponse<?> login(@Valid @RequestBody LoginRequestDTO loginRequestDTO) {
        authService.login(loginRequestDTO);
        return ApiResponse.builder()
                .status(HttpStatus.ACCEPTED.value())
                .message("Successfully logged in")
                .build();
    }

    @PostMapping("/register")
    public ResponseData<?> register(@Valid @RequestBody RegisterRequestDTO registerRequestDTO) {
        try {
            authService.register(registerRequestDTO);
            return new ResponseData<>(HttpStatus.ACCEPTED.value(), "Đăng ký thành công! Xác nhận email để kích hoạt tài khoản");
        } catch (Exception e) {
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    @PatchMapping("/confirm")
    public ResponseData<?> confirm(@RequestParam String verifyCode) {
        try {
            String message = authService.confirmUser(verifyCode);
            return new ResponseData<>(HttpStatus.OK.value(), "Confirm successfully", message);
        } catch (Exception e) {
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Confirm failed");
        }
    }

    @PostMapping("/logout")
    public ResponseData<?> logout() {
        try {
            String message = authService.logout();
            return new ResponseData<>(HttpStatus.OK.value(), "Logout successfully", message);
        } catch (Exception e) {
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Logout failed");
        }
    }

    @PostMapping("/forgot-password")
    public ResponseData<?> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
        try {
            String message = authService.forgotPassword(request.getEmail());
            return new ResponseData<>(HttpStatus.ACCEPTED.value(), "Loading", message);
        } catch (Exception e) {
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Forgot password failed");
        }
    }

    @PostMapping("/reset-password")
    public ResponseData<?> resetPassword(@Valid @RequestBody ResetPasswordRequest request, @RequestParam String verifyCode) {
        try {
            authService.resetPassword(request, verifyCode);
            return new ResponseData<>(HttpStatus.ACCEPTED.value(), "Reset password successfully");
        } catch (Exception e) {
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Reset password failed");
        }
    }
}
