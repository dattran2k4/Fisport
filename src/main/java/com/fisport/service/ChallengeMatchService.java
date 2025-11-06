package com.fisport.service;

import com.fisport.common.EChallengeStatus;
import com.fisport.common.ELevel;
import com.fisport.dto.request.ChallengeMatchCreateRequest;
import com.fisport.dto.request.ChallengeMatchUpdateRequest;
import com.fisport.dto.response.*;
import com.fisport.model.ChallengeMatch;

import java.util.List;

public interface ChallengeMatchService {

    void createChallengeMatch(ChallengeMatchCreateRequest request, String username);

    void updateChallengeMatch(Long id, ChallengeMatchUpdateRequest request, String username);

    ChallengeMatchDetailResponse getChallengeMatchDetail(Long id);

    PageResponse<ChallengeMatchSummaryResponse> getAllChallengeMatch(EChallengeStatus status, ELevel level,
                                                                     Long typeId, Long cityId, Long fieldTypeId, int page, int size);

    ChallengeMatch findChallengeMatch(Long id);

    void updateMatchStatus(ChallengeMatch challengeMatch);

    void checkMatchFinished(Long matchId);

    void cancelMatch(Long matchId, String username);

    boolean canCancel(Long matchId);

    List<ChallengeMatchManagementResponse> getListMatchForManagement(String username);

    ChallengeMatchDetailManagementResponse getMatchDetailForManagement(Long matchId, String username);
}
