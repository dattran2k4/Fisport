package com.Fisport.api;

import com.Fisport.dto.request.LoginRequestDTO;
import com.Fisport.dto.request.RegisterRequestDTO;
import com.Fisport.dto.response.LoginResponseDTO;
import com.Fisport.dto.response.RegisterResponseDTO;
import com.Fisport.dto.response.ResponseData;
import com.Fisport.dto.response.ResponseError;
import com.Fisport.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthApiController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseData<?> login(@Valid @RequestBody LoginRequestDTO loginRequestDTO) {
        try {
            LoginResponseDTO loginResponseDTO = authService.login(loginRequestDTO);
            return new ResponseData<>(HttpStatus.ACCEPTED.value(), "Đăng nhập thành công!",  loginResponseDTO);
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
