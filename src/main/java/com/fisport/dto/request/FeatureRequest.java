package com.fisport.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class FeatureRequest {

    @NotBlank(message = "Không được để trống tên tiện ích")
    private String name;
}
