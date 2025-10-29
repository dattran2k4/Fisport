package com.fisport.dto.response;

import com.fisport.common.EChallengeStatus;
import lombok.*;

import java.time.LocalDate;

@Getter
@Builder
public class ChallengeMatchManagementResponse {
    private Long id;
    private String title;
    private EChallengeStatus status;
    private Integer playersInMatch;
    private Integer maxPlayers;
    private Integer playerPaidCount;
    private Integer playersPending;
    private String matchType;
    private LocalDate date;
    private String sport;
}
