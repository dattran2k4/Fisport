package com.fisport.dto.response;

import com.fisport.common.ETeam;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ChallengeResultResponse {
    private Long matchId;
    private Integer scoreTeamA;
    private Integer scoreTeamB;
    private boolean hasResult;
}
