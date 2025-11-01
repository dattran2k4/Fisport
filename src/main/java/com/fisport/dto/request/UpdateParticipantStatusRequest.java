package com.fisport.dto.request;

import com.fisport.common.EParticipantStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class UpdateParticipantStatusRequest {
    @NotBlank(message = "Hãy gửi thông điệp đến họ")
    @Size(max = 50, message = "Chỉ được nhập 50 kí tự")
    private String responseMessage;
}
