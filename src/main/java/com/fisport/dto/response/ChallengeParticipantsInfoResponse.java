package com.fisport.dto.response;

import com.fisport.common.EGender;
import com.fisport.common.ELevel;
import com.fisport.common.ETeam;
import lombok.*;

@Getter
@Builder
public class ChallengeParticipantsInfoResponse {
    private Long id;
    private String username;
    private ELevel playerLevel;
    private ETeam team;
    private Integer playerElo;
    private EGender playerGender;
}
