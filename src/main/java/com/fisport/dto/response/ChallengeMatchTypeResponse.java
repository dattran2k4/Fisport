package com.fisport.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ChallengeMatchTypeResponse {
    private Long id;
    private String name;
    private Integer playersPerTeam;
}
