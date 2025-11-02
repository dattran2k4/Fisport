package com.fisport.service;

import com.fisport.common.EParticipantStatus;
import com.fisport.common.ETeam;
import com.fisport.dto.request.JoinMatchRequest;
import com.fisport.dto.request.UpdateParticipantStatusRequest;
import com.fisport.dto.response.ChallengeParticipantForCreatorResponse;
import com.fisport.dto.response.ChallengeParticipantsForUserResponse;
import com.fisport.dto.response.ChallengeParticipantsInfoResponse;
import com.fisport.model.ChallengeParticipant;

import java.util.List;

public interface ChallengeParticipantService {

    void joinMatch(Long matchId, JoinMatchRequest joinMatchRequest, String username);

    String acceptPlayer(Long participantId, UpdateParticipantStatusRequest request, String username);

    String rejectPlayer(Long participantId, UpdateParticipantStatusRequest request, String username);

    void markNotJoinPlayer(Long participantId, String username);

    List<ChallengeParticipantForCreatorResponse> getAllParticipantsByMatchAndCreator(Long matchId, String username);

    //Web-detail
    List<ChallengeParticipantsInfoResponse> getAllAcceptedParticipantsInfo(Long matchId);

    List<ChallengeParticipantsForUserResponse> getAllPariticipantsByUser(String username);

    Integer getAcceptedCurrentPlayers(Long matchId);

    Integer getPendingCurrentPlayers(Long matchId);

    Integer countAcceptedPlayersByTeam(Long matchId, ETeam team);

    List<ChallengeParticipant> getParticipantsByMatchAndTeamAndStatus(Long matchId, ETeam team, EParticipantStatus status);
}
