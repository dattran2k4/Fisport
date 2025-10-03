package com.Fisport.api;

import com.Fisport.dto.request.LoginRequestDTO;
import com.Fisport.dto.request.RegisterRequestDTO;
import com.Fisport.dto.response.*;
import com.Fisport.service.AuthService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@Validated
@RequestMapping("/api/auth")
public class AuthApiController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseData<?> login(@Valid @RequestBody LoginRequestDTO loginRequestDTO, HttpSession session) {
        try {
            LoginResponse loginResponse = authService.loginApi(loginRequestDTO, session);
            session.setAttribute("loginResponse", loginResponse);
            return new ResponseData<>(HttpStatus.ACCEPTED.value(), "Đăng nhập thành công!",  loginResponse);
        }
        catch (Exception e) {
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Đăng nhập thất bại");
        }
    }

    @PostMapping("/register")
    public ResponseData<?> register(@Valid @RequestBody RegisterRequestDTO registerRequestDTO) {
        try  {
            RegisterResponseDTO registerResponseDTO = authService.register(registerRequestDTO);
            return new ResponseData<>(HttpStatus.ACCEPTED.value(), "Đăng ký thành công! Xác nhận email để kích hoạt tài khoản", registerResponseDTO);
        }
        catch (Exception e) {
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    @PatchMapping("/confirm/{userId}")
    public ResponseData<?> confirm(@Min(1) @PathVariable Long userId, @RequestParam String verifyCode) {
        try {
            String message = authService.confirmUser(userId, verifyCode);
            return new ResponseData<>(HttpStatus.OK.value(), "Confirm successfully", message);
        } catch (Exception e) {
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Confirm failed");
        }
    }
}
