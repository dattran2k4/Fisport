package com.fisport.dto.response;

import com.fisport.common.EChallengeStatus;
import com.fisport.common.EGender;
import com.fisport.common.ELevel;
import com.fisport.common.ETeam;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Builder
@Getter
public class ChallengeMatchDetailResponse {
    private Long id;
    private String title;
    private String note;
    private ELevel level;
    private EChallengeStatus challengeStatus;
    private Integer maxPlayers;
    private Integer currentPlayers;
    private String matchType;
    private String wardName;
    private String cityName;
    private String fieldName;
    private BigDecimal fee;
    private LocalTime startTime;
    private LocalTime endTime;
    private LocalDate date;
    private LocalDateTime createdAt;
    private String creatorUsername;
    private Integer creatorElo;
    private EGender creatorGender;
    private ELevel creatorLevel;
    private String sport;
    private Map<ETeam, List<ChallengeParticipantsInfoResponse>> participants = new HashMap<>();
}
