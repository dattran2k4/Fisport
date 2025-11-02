package com.fisport.service;

import com.fisport.dto.request.MatchResultRequest;
import com.fisport.dto.response.ChallengeResultResponse;

import java.util.List;

public interface ChallengeResultService {
    void updateMatchResult(Long matchID, MatchResultRequest request, String username);

    List<ChallengeResultResponse> getAll(Long matchId);

    ChallengeResultResponse getByMatchId(Long matchId);
}
