package com.fisport.service.impl;

import com.fisport.common.EParticipantStatus;
import com.fisport.dto.request.JoinMatchRequest;
import com.fisport.dto.request.UpdateParticipantStatusRequest;
import com.fisport.dto.response.ChallengeParticipantResponse;
import com.fisport.exception.InvalidDataException;
import com.fisport.exception.ResourceNotFoundException;
import com.fisport.model.ChallengeMatch;
import com.fisport.model.ChallengeParticipant;
import com.fisport.model.User;
import com.fisport.repository.ChallengeMatchRepository;
import com.fisport.repository.ChallengeParticipantRepository;
import com.fisport.repository.UserRepository;
import com.fisport.service.ChallengeMatchService;
import com.fisport.service.ChallengeParticipantService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class ChallengeParticipantServiceImpl implements ChallengeParticipantService {

    private final UserRepository userRepository;
    private final ChallengeMatchRepository  challengeMatchRepository;
    private final ChallengeParticipantRepository challengeParticipantRepository;
    private final ChallengeMatchService  challengeMatchService;

    @Override
    @Transactional
    public void joinMatch(Long matchId, JoinMatchRequest joinMatchRequest, String username) {

        User user = userRepository.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy user"));

        ChallengeMatch challengeMatch = challengeMatchRepository.findById(matchId).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy trận đấu"));

        ChallengeParticipant challengeParticipant =  ChallengeParticipant.builder()
                .match(challengeMatch)
                .user(user)
                .status(EParticipantStatus.PENDING)
                .requestMessage(joinMatchRequest.getMessage())
                .paid(false)
                .build();

        challengeParticipantRepository.save(challengeParticipant);
        log.info("User id {} request join match id {}", user.getId(), matchId);
    }

    @Override
    @Transactional
    public void responeMatch(Long participantId, UpdateParticipantStatusRequest request, String username) {


        ChallengeParticipant participant = findParticipant(participantId);

        if (!participant.getMatch().getCreator().getUsername().equals(username)) {
            throw new InvalidDataException("Chỉ có người tạo trận đấu mới được thực hiện");
        }

        participant.setStatus(request.getStatus());
        participant.setResponseMessage(request.getResponseMessage());

        challengeParticipantRepository.save(participant);

        challengeMatchService.updateMatchStatus(participant.getMatch());
        log.info("MatchId {} status is: {}" , participant.getMatch().getId(), participant.getMatch().getStatus());

        log.info("creator {} response for playerId {}", username, participant.getUser().getId());
    }

    @Override
    public List<ChallengeParticipantResponse> getAllParticipantsByMatchAndCreator(Long matchId, String username) {
        ChallengeMatch match = challengeMatchRepository.findById(matchId).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy trận đấu"));

        if (!match.getCreator().getUsername().equals(username)) {
            throw new InvalidDataException("Chỉ có người tạo trận đấu mới được thực hiện");
        }

        return match.getParticipants().stream().map(p -> ChallengeParticipantResponse.builder()
                .id(p.getId())
                .username(p.getUser().getUsername())
                .status(p.getStatus())
                .message(p.getRequestMessage())
                .paid(p.isPaid())
                .build()).toList();
    }

    private ChallengeParticipant findParticipant(Long id) {
        return challengeParticipantRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy sự tham gia nào cả"));
    }


}
