package com.fisport.dto.response;

import com.fisport.common.EChallengeStatus;
import com.fisport.common.ELevel;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Builder
public class ChallengeMatchDetailManagementResponse {
    private Long id;
    private String title;
    private String note;
    private ELevel level;
    private EChallengeStatus challengeStatus;
    private Integer playerTeamA;
    private Integer playerTeamB;
    private Integer maxPlayersPerTeam;
    private String matchType;
    private String ward;
    private String city;
    private String field;
    private String subField;
    private BigDecimal fee;
    private LocalTime startTime;
    private LocalTime endTime;
    private LocalDate date;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String sport;
}
