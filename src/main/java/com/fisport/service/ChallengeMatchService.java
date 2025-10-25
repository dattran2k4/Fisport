package com.fisport.service;

import com.fisport.dto.request.ChallengeMatchRequest;

public interface ChallengeMatchService {

    String createChallengeMatch(ChallengeMatchRequest request, String username);
}
