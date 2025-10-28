package com.fisport.service;

import com.fisport.common.ETeam;
import com.fisport.dto.request.JoinMatchRequest;
import com.fisport.dto.request.UpdateParticipantStatusRequest;
import com.fisport.dto.response.ChallengeParticipantForCreatorResponse;
import com.fisport.dto.response.ChallengeParticipantsForUserResponse;
import com.fisport.dto.response.ChallengeParticipantsInfoResponse;

import java.util.List;
import java.util.Map;

public interface ChallengeParticipantService {

    void joinMatch(Long matchId, JoinMatchRequest joinMatchRequest, String username);

    void responeMatch(Long participantId, UpdateParticipantStatusRequest request, String username);

    List<ChallengeParticipantForCreatorResponse> getAllParticipantsByMatchAndCreator(Long matchId, String username);

    Map<ETeam, List<ChallengeParticipantsInfoResponse>> getAllAcceptedParticipantsInfo(Long matchId);

//    Map<ETeam, List<ChallengeParticipant>> getAllAcceptedPariticipants

    List<ChallengeParticipantsForUserResponse> getAllPariticipantsByUser(String username);

    
}
