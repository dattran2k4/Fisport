package com.fisport.api;

import com.fisport.dto.request.ForgotPasswordRequest;
import com.fisport.dto.request.LoginRequestDTO;
import com.fisport.dto.request.RegisterRequestDTO;
import com.fisport.dto.request.ResetPasswordRequest;
import com.fisport.dto.response.*;
import com.fisport.service.AuthService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;

@RequiredArgsConstructor
@RestController
@Validated
@RequestMapping("/api/auth")
public class AuthApiController {
    private final AuthService authService;

    @PostMapping("/login")
    public ApiResponse login(@Valid @RequestBody LoginRequestDTO loginRequestDTO) {
        LoginResponse response = authService.login(loginRequestDTO);
        return ApiResponse.builder()
                .status(HttpStatus.ACCEPTED.value())
                .message("Successfully logged in")
                .data(response)
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
    public ResponseData<?> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) throws MessagingException, UnsupportedEncodingException {
        authService.forgotPassword(request.getEmail());

        try {
            return new ResponseData<>(HttpStatus.OK.value(),
                    "Đã gửi tín hiệu vũ trụ đến email của bạn. Vui lòng kiểm tra để tạo lại mật khẩu");
        } catch (Exception e) {
            return new ResponseError(HttpStatus.NOT_FOUND.value(), e.getMessage());
        }
    }


    @PostMapping("/reset-password")
    public ResponseData<?> resetPassword(@Valid @RequestBody ResetPasswordRequest request, @RequestParam String verifyCode) {
        authService.resetPassword(request, verifyCode);
        try {
            return new ResponseData<>(HttpStatus.OK.value(), "Tạo lại thành công");
        } catch (Exception e) {
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }
}
