package com.fisport.service;

import com.fisport.dto.request.MatchResultRequest;

public interface ChallengeResultService {
    void updateMatchResult(Long matchID, MatchResultRequest request, String username);
}
