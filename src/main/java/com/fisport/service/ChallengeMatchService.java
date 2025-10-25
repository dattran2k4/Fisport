package com.fisport.service;

import com.fisport.common.EChallengeStatus;
import com.fisport.common.ELevel;
import com.fisport.dto.request.ChallengeMatchRequest;
import com.fisport.dto.response.ChallengeMatchResponse;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface ChallengeMatchService {

    String createChallengeMatch(ChallengeMatchRequest request, String username);

    ChallengeMatchResponse getChallengeMatch(Long id);

    Page<ChallengeMatchResponse> getAllChallengeMatch(EChallengeStatus status, ELevel level,
                                                      Integer maxPlayers, LocalDate date, BigDecimal fee, Long cityId, Long fieldTypeId, int page, int size);
}
