package com.fisport.service;

import com.fisport.common.EChallengeStatus;
import com.fisport.common.ELevel;
import com.fisport.dto.request.ChallengeMatchRequest;
import com.fisport.dto.response.ChallengeMatchDetailResponse;
import com.fisport.dto.response.ChallengeMatchManagementResponse;
import com.fisport.dto.response.ChallengeMatchSummaryResponse;
import com.fisport.dto.response.PageResponse;
import com.fisport.model.ChallengeMatch;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface ChallengeMatchService {

    void createChallengeMatch(ChallengeMatchRequest request, String username);

    ChallengeMatchDetailResponse getChallengeMatchDetail(Long id);

    PageResponse<ChallengeMatchSummaryResponse> getAllChallengeMatch(EChallengeStatus status, ELevel level,
                                                                     String matchType, LocalDate date, BigDecimal fee, Long cityId, Long fieldTypeId, int page, int size);

    ChallengeMatch findChallengeMatch(Long id);

    void updateMatchStatus(ChallengeMatch challengeMatch);

    void checkMatchFinished(Long matchId);

    void cancelMatch(Long matchId);

    boolean canCancel(Long matchId);

    List<ChallengeMatchManagementResponse> getListMatchForManagement(String username);
}
