package com.fisport.dto.request;

import com.fisport.common.EParticipantStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class UpdateParticipantStatusRequest {
    @NotNull
    private EParticipantStatus status; // ACCEPTED / REJECTED

    private String responseMessage;
}
