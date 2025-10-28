package com.fisport.dto.response;

import com.fisport.common.EGender;
import com.fisport.common.ELevel;
import com.fisport.common.EParticipantStatus;
import com.fisport.common.ETeam;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public class ChallengeParticipantForCreatorResponse {
    private Long id;
    private String username;
    private ELevel playerLevel;
    private Integer playerElo;
    private EGender playerGender;
    private String message;
    private ETeam team;
    private boolean paid;
    private EParticipantStatus status;
    private LocalDateTime createdAt;
}
