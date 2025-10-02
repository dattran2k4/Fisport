package com.Fisport.api;

import com.Fisport.dto.request.LoginRequestDTO;
import com.Fisport.dto.request.RegisterRequestDTO;
import com.Fisport.dto.response.*;
import com.Fisport.service.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
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
            return new ResponseData<>(HttpStatus.ACCEPTED.value(), "Đăng ký thành công!", registerResponseDTO);
        }
        catch (Exception e) {
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Đăng ký thất bại!");
        }
    }

    @PatchMapping("/confirm/{userId}")
    public ResponseData<?> confirm(Long userId, @RequestParam String verifyCode) {
        try {
            authService.confirmUser(userId, verifyCode);
            return new ResponseData<>(HttpStatus.OK.value(), "Confirm successfully");
        } catch (Exception e) {
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Confirm failed");
        }
    }
}
