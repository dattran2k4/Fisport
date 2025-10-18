package com.Fisport.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
public class TwoFARequest implements Serializable {
    private String username;

    @NotBlank(message = "Mã 2FA không được để trống")
    private String code;
}
