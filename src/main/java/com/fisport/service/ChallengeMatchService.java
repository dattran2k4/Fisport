package com.fisport.service;

import com.fisport.common.EChallengeStatus;
import com.fisport.common.ELevel;
import com.fisport.dto.request.ChallengeMatchRequest;
import com.fisport.dto.response.ChallengeMatchDetailResponse;
import com.fisport.dto.response.ChallengeMatchSummaryResponse;
import com.fisport.model.ChallengeMatch;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface ChallengeMatchService {

    void createChallengeMatch(ChallengeMatchRequest request, String username);

    ChallengeMatchDetailResponse getChallengeMatchDetail(Long id);

    Page<ChallengeMatchSummaryResponse> getAllChallengeMatch(EChallengeStatus status, ELevel level,
                                                             String matchType, LocalDate date, BigDecimal fee, Long cityId, Long fieldTypeId, int page, int size);

    void updateMatchStatus(ChallengeMatch challengeMatch);

    Integer getCurrentPlayers(Long matchId);

    void checkMatchFinished(Long matchId);
}
