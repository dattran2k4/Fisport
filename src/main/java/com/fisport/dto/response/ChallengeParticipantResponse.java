package com.fisport.dto.response;

import com.fisport.common.EParticipantStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public class ChallengeParticipantResponse {
    private Long id;
    private String username;
    private String message;
    private boolean paid;
    private EParticipantStatus status;
    private LocalDateTime createdAt;
}
