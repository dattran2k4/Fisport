package com.fisport.dto.request;

import com.fisport.common.ESubFieldStatus;
import com.fisport.dto.validator.EnumValue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class SubFieldRequest {
    @NotBlank(message = "Không được để trống")
    private String name;

    @EnumValue(name = "status", enumClass =  ESubFieldStatus.class)
    @NotNull(message = "Không được để trống")
    private ESubFieldStatus status;

    @NotNull(message = "Không được để trống")
    private Long fieldId;
}
