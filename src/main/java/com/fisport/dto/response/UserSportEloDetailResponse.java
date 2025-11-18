package com.fisport.dto.response;

import com.fisport.common.ELevel;
import lombok.*;

@Getter
@Setter
@Builder
public class UserSportEloDetailResponse {
    private String sportName;
    private Integer elo;
    private ELevel level;

}
