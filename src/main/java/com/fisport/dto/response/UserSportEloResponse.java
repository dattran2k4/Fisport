package com.fisport.dto.response;

import com.fisport.common.ELevel;
import lombok.*;

@Getter
@Builder
public class UserSportEloResponse {
    private Integer elo;
    private ELevel level;
}
