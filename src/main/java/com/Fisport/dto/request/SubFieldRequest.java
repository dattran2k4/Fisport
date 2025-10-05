package com.Fisport.dto.request;

import com.Fisport.common.ESubFieldStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class SubFieldRequest {
    @NotBlank(message = "Không được để trống")
    private String name;

    @NotBlank(message = "Không được để trống")
    private ESubFieldStatus status;

    @NotNull(message = "Không được để trống")
    private Long fieldId;
}
