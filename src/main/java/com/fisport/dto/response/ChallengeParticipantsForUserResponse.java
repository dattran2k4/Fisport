package com.fisport.dto.response;

import com.fisport.common.EParticipantStatus;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Builder
public class ChallengeParticipantsForUserResponse {
    private Long matchId;
    private String title;
    private String fieldName;
    private String sport;
    private String matchType;
    private LocalDate date;
    private BigDecimal fee;
    private EParticipantStatus status;
    private boolean canPay;
    private boolean isPaid;
}
