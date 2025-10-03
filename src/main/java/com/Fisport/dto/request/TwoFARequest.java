package com.Fisport.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

import java.io.Serializable;

@Getter
public class TwoFARequest implements Serializable {
    private String username;

    @NotBlank(message = "Mã 2FA không được để trống")
    @Pattern(   regexp = "\\d{6}",
                message = "Mã 2FA phải gồm 6 chữ số")
    private int code;
}
