package com.Fisport.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

import java.io.Serializable;

@Getter
public class LoginRequestDTO implements Serializable {

    @NotBlank(message = "tên đăng nhập không được để trống")
    private String username;

    @NotBlank(message = "mật khẩu không được để trống")
    private String password;
}
