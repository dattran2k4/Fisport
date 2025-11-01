package com.fisport.dto.response;

import com.fisport.common.EChallengeStatus;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
public class ChallengeMatchManagementResponse {
    private Long id;
    private String title;
    private EChallengeStatus status;
    private Integer playerTeamA;
    private Integer playerTeamB;
    private Integer maxPlayersPerTeam;
    private String matchType;
    private LocalDate date;
    private String sport;
    private List<String> actions;
}
