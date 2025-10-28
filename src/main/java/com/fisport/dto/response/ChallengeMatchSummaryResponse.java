package com.fisport.dto.response;

import com.fisport.common.EChallengeStatus;
import com.fisport.common.ELevel;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Builder
public class ChallengeMatchSummaryResponse {
    private Long id;
    private String matchType;
    private String title;
    private Integer currentPlayers;
    private Integer maxPlayers;
    private BigDecimal fee;
    private String fieldName;
    private String city;
    private String ward;
    private LocalDate date;
    private EChallengeStatus status;
    private String sport;
    private String level;
}
