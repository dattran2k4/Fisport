package com.fisport.dto.response;

import com.fisport.common.EChallengeStatus;
import com.fisport.common.ELevel;
import lombok.Builder;
import lombok.Getter;

import java.time.*;


@Builder
@Getter
public class ChallengeMatchResponse {
    private Long id;
    private String title;
    private String note;
    private ELevel level;
    private EChallengeStatus challengeStatus;
    private int maxPlayers;
    private FieldTypeResponse fieldTypeResponse;
    private LocalTime startTime;
    private LocalTime endTime;
    private LocalDate date;
    private LocalDateTime createdAt;
}
