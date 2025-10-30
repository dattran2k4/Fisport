package com.fisport.service;

import com.fisport.dto.request.JoinMatchRequest;
import com.fisport.dto.request.UpdateParticipantStatusRequest;
import com.fisport.dto.response.ChallengeParticipantResponse;

import java.util.List;

public interface ChallengeParticipantService {

    void joinMatch(Long matchId, JoinMatchRequest joinMatchRequest, String username);

    void responeMatch(Long participantId, UpdateParticipantStatusRequest request, String username);

    List<ChallengeParticipantResponse> getAllParticipantsByMatchAndCreator(Long matchId, String username);

}
