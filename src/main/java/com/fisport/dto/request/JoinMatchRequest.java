package com.fisport.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class JoinMatchRequest {

    @NotBlank(message = "Nhập nội dung để gửi người đó")
    private String message;
}
