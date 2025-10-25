package com.fisport.service;

import com.fisport.dto.request.ChallengeMatchRequest;
import com.fisport.dto.response.ChallengeMatchResponse;
import org.springframework.data.domain.Page;

public interface ChallengeMatchService {

    String createChallengeMatch(ChallengeMatchRequest request, String username);

    ChallengeMatchResponse getChallengeMatch(Long id);

    Page<ChallengeMatchResponse> getAllChallengeMatch();
}
