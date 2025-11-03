package com.fisport.dto.request;

import com.fisport.common.ETeam;
import com.fisport.dto.validator.EnumValue;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class JoinMatchRequest {

    @NotBlank(message = "Nhập nội dung để gửi người đó")
    private String message;

    @EnumValue(name = "team", enumClass = ETeam.class)
    private ETeam team;
}
