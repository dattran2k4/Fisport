package com.fisport.service.impl;

import com.fisport.common.EChallengeStatus;
import com.fisport.common.EParticipantStatus;
import com.fisport.common.ETeam;
import com.fisport.dto.request.JoinMatchRequest;
import com.fisport.dto.request.UpdateParticipantStatusRequest;
import com.fisport.dto.response.ChallengeParticipantForCreatorResponse;
import com.fisport.dto.response.ChallengeParticipantsForUserResponse;
import com.fisport.dto.response.ChallengeParticipantsInfoResponse;
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
import com.fisport.service.UserSportEloService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
public class ChallengeParticipantServiceImpl implements ChallengeParticipantService {

    private final UserRepository userRepository;
    private final ChallengeMatchRepository challengeMatchRepository;
    private final ChallengeParticipantRepository challengeParticipantRepository;
    private final ChallengeMatchService challengeMatchService;
    private final UserSportEloService userSportEloService;

    @Override
    @Transactional
    public void joinMatch(Long matchId, JoinMatchRequest joinMatchRequest, String username) {

        User user = userRepository.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy user"));

        ChallengeMatch challengeMatch = challengeMatchRepository.findById(matchId).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy trận đấu"));

        ChallengeParticipant challengeParticipant = ChallengeParticipant.builder()
                .match(challengeMatch)
                .user(user)
                .status(EParticipantStatus.PENDING)
                .requestMessage(joinMatchRequest.getMessage())
                .team(joinMatchRequest.getTeam())
                .paid(false)
                .build();

        challengeParticipantRepository.save(challengeParticipant);
        log.info("User id {} request join team {} for matchId {}, ", user.getId(), joinMatchRequest.getTeam(), matchId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void responeMatch(Long participantId, UpdateParticipantStatusRequest request, String username) {


        ChallengeParticipant participant = findParticipant(participantId);

        if (!participant.getMatch().getCreator().getUsername().equals(username)) {
            throw new InvalidDataException("Chỉ có người tạo trận đấu mới được thực hiện");
        }

        //Check số người chơi hiện tại < max
        if (request.getStatus().equals(EParticipantStatus.ACCEPTED)) {
            if (participant.getMatch().getStatus().equals(EChallengeStatus.FULL)) {
                throw new InvalidDataException("Số lượng người tham gia hiện tại đã đủ");
            }
        }

        participant.setStatus(request.getStatus());
        participant.setResponseMessage(request.getResponseMessage());

        challengeParticipantRepository.save(participant);

        challengeMatchService.updateMatchStatus(participant.getMatch());
        log.info("MatchId {} status is: {}", participant.getMatch().getId(), participant.getMatch().getStatus());

        log.info("creator {} response for playerId {}", username, participant.getUser().getId());
    }

    @Override
    public List<ChallengeParticipantForCreatorResponse> getAllParticipantsByMatchAndCreator(Long matchId, String username) {
        ChallengeMatch match = challengeMatchRepository.findById(matchId).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy trận đấu"));

        return match.getParticipants().stream().map(p -> ChallengeParticipantForCreatorResponse.builder()
                .id(p.getId())
                .username(p.getUser().getUsername())
                .status(p.getStatus())
                .playerGender(p.getUser().getGender())
                .playerElo(userSportEloService.getElo(p.getUser().getId(), match.getBooking().getSubfield().getField().getFieldType().getId()))
                .playerLevel(userSportEloService.getLevel(p.getUser().getId(), match.getBooking().getSubfield().getField().getFieldType().getId()))
                .team(p.getTeam())
                .message(p.getRequestMessage())
                .paid(p.isPaid())
                .build()).toList();
    }

    @Override
    public Map<ETeam, List<ChallengeParticipantsInfoResponse>> getAllAcceptedParticipantsInfo(Long matchId) {
        List<ChallengeParticipantsInfoResponse> participants = challengeParticipantRepository.findAllAcceptedParticipantsInfo(matchId);

        return participants.stream().collect(Collectors.groupingBy(ChallengeParticipantsInfoResponse::getTeam));
    }

    @Override
    public List<ChallengeParticipantsForUserResponse> getAllPariticipantsByUser(String username) {
        List<ChallengeParticipant> participants = challengeParticipantRepository.findAllWithMatchByUser(username);

        return participants.stream().map(p -> ChallengeParticipantsForUserResponse.builder()
                .matchId(p.getMatch().getId())
                .fee(p.getMatch().getParticipationFee())
                .date(p.getMatch().getBooking().getBookingDate())
                .sport(p.getMatch().getBooking().getSubfield().getField().getFieldType().getName())
                .title(p.getMatch().getTitle())
                .isPaid(p.isPaid())
                .canPay(p.getStatus().equals(EParticipantStatus.ACCEPTED) && !p.isPaid())
                .build()).toList();
    }

    private ChallengeParticipant findParticipant(Long id) {
        return challengeParticipantRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy sự tham gia nào cả"));
    }


}
