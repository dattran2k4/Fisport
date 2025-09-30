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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@Validated
@RequestMapping("/api/auth")
public class AuthApiController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseData<?> login(@Valid @RequestBody LoginRequestDTO loginRequestDTO, HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
        try {
            LoginResponse loginResponse = authService.loginApi(loginRequestDTO, httpRequest, httpResponse);
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
}
