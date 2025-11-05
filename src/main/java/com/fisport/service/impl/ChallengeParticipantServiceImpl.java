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
import com.fisport.service.ChallengeMatchTypeService;
import com.fisport.service.ChallengeParticipantService;
import com.fisport.service.UserSportEloService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class ChallengeParticipantServiceImpl implements ChallengeParticipantService {

    private final UserRepository userRepository;
    private final ChallengeMatchRepository challengeMatchRepository;
    private final ChallengeMatchTypeService challengeMatchTypeService;
    private final ChallengeParticipantRepository challengeParticipantRepository;
    private final UserSportEloService userSportEloService;

    @Override
    @Transactional
    public void joinMatch(Long matchId, JoinMatchRequest joinMatchRequest, String username) {

        User user = userRepository.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy user"));

        ChallengeMatch challengeMatch = challengeMatchRepository.findById(matchId).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy trận đấu"));

        int maxPlayerPerTeam = challengeMatch.getChallengeMatchType().getMaxPlayers() / 2;

        if (challengeMatch.getStatus().equals(EChallengeStatus.CANCELLED)) {
            throw new InvalidDataException("Trận đấu đã bị huỷ");
        }

        LocalDate date = challengeMatch.getBooking().getBookingDate();
        LocalTime startTime = challengeMatch.getBooking().getStartTime();

        LocalDateTime start = LocalDateTime.of(date, startTime);

        if (start.isBefore(LocalDateTime.now())) {
            throw new InvalidDataException("Trận đấu đã bắt đầu");
        }

        if (challengeParticipantRepository.existsByMatchIdAndUserId(matchId, user.getId())) {
            throw new InvalidDataException("Bạn đã tham gia rồi");
        }

        if (getAcceptedCurrentPlayers(matchId) == challengeMatchTypeService.maxPlayer(challengeMatch.getChallengeMatchType().getId())) {
            throw new InvalidDataException("Số lượng người tham gia hiện tại đã đủ");
        }

        if (joinMatchRequest.getTeam().equals(ETeam.TEAM_A) && (countAcceptedPlayersByTeam(matchId, ETeam.TEAM_A).compareTo(maxPlayerPerTeam) >= 0)) {
            throw new InvalidDataException("Đội A đã đủ người");
        }

        if (joinMatchRequest.getTeam().equals(ETeam.TEAM_B) && (countAcceptedPlayersByTeam(matchId, ETeam.TEAM_B).compareTo(maxPlayerPerTeam) >= 0)) {
            throw new InvalidDataException("Đội B đã đủ người");
        }

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
    public String acceptPlayer(Long participantId, UpdateParticipantStatusRequest request, String username) {

        ChallengeParticipant participant = findParticipant(participantId);

        if (!participant.getMatch().getCreator().getUsername().equals(username)) {
            throw new InvalidDataException("Chỉ có người tạo trận đấu mới được thực hiện");
        }

        if (getAcceptedCurrentPlayers(participant.getMatch().getId()).compareTo(participant.getMatch().getChallengeMatchType().getMaxPlayers()) >= 0) {
            throw new InvalidDataException("Số lượng người chơi đã đủ!");
        }

        BigDecimal fee = participant.getMatch().getParticipationFee();

        if (fee == null || fee.compareTo(BigDecimal.ZERO) == 0) {
            participant.setPaid(true);
            participant.setPaidAt(LocalDateTime.now());
        }

        participant.setStatus(EParticipantStatus.ACCEPTED);
        participant.setResponseMessage(request.getResponseMessage());

        challengeParticipantRepository.save(participant);

        int currentAcceptedPlayer = getAcceptedCurrentPlayers(participant.getMatch().getId());
        if (currentAcceptedPlayer == participant.getMatch().getChallengeMatchType().getMaxPlayers()) {
            participant.getMatch().setStatus(EChallengeStatus.FULL);
            challengeMatchRepository.save(participant.getMatch());
            log.info("MatchId {} is FULL", participant.getMatch().getId());
        }

        log.info("creator {} accepted for playerId {}", username, participant.getUser().getId());

        return participant.getUser().getUsername();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String rejectPlayer(Long participantId, UpdateParticipantStatusRequest request, String username) {
        ChallengeParticipant participant = findParticipant(participantId);

        if (!participant.getMatch().getCreator().getUsername().equals(username)) {
            throw new InvalidDataException("Chỉ có người tạo trận đấu mới được thực hiện");
        }

        participant.setStatus(EParticipantStatus.REJECTED);
        participant.setResponseMessage(request.getResponseMessage());

        ChallengeMatch match = participant.getMatch();

        int currentAcceptedPlayer = getAcceptedCurrentPlayers(match.getId());
        if (match.getStatus().equals(EChallengeStatus.FULL) && currentAcceptedPlayer < match.getChallengeMatchType().getMaxPlayers()) {
            match.setStatus(EChallengeStatus.PENDING);
            challengeMatchRepository.save(match);
            log.info("MatchId {} is change from FULL to PENDING", match.getId());
        }

        challengeParticipantRepository.save(participant);
        log.info("creator {} rejected for playerId {}", username, participant.getUser().getId());

        return participant.getUser().getUsername();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void markNotJoinPlayer(Long participantId, String username) {
        ChallengeParticipant participant = findParticipant(participantId);

        ChallengeMatch match = participant.getMatch();

        if (!match.getCreator().getUsername().equals(username)) {
            throw new InvalidDataException("Chỉ có người tạo trận đấu mới được thực hiện");
        }

        //check finish
        if (match.getBooking().getEndTime().isBefore(LocalTime.now()) || match.getStatus().equals(EChallengeStatus.MATCHED)) {
            throw new InvalidDataException("Chưa hoàn thành trận đấu");
        }

        participant.setStatus(EParticipantStatus.NOSHOW);
        challengeParticipantRepository.save(participant);
        log.info("PlayerId {} marked not shown", participant.getUser().getId());
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
                .actions(checkAction(p))
                .build()).toList();
    }

    private List<String> checkAction(ChallengeParticipant p) {
        List<String> actions = new ArrayList<>();

        switch (p.getStatus()) {
            case PENDING:
                actions.add("Chấp nhận");
                actions.add("Từ chối");
                break;
            case ACCEPTED:
                actions.add("Không tham gia");
                break;
            case NOSHOW:
            case REJECTED:
            case CANCELLED:
        }

        return actions;
    }

    @Override
    public List<ChallengeParticipantsInfoResponse> getAllAcceptedParticipantsInfo(Long matchId) {
        return challengeParticipantRepository.findAllAcceptedParticipantsInfo(matchId);
    }

    @Override
    public List<ChallengeParticipantsForUserResponse> getAllPariticipantsByUser(String username) {
        List<ChallengeParticipant> participants = challengeParticipantRepository.findAllWithMatchByUser(username);

        return participants.stream().map(p -> ChallengeParticipantsForUserResponse.builder()
                .matchId(p.getMatch().getId())
                .fee(p.getMatch().getParticipationFee())
                .date(p.getMatch().getBooking().getBookingDate())
                .fieldName(p.getMatch().getBooking().getSubfield().getField().getName())
                .sport(p.getMatch().getBooking().getSubfield().getField().getFieldType().getName())
                .title(p.getMatch().getTitle())
                .isPaid(p.isPaid())
                .status(p.getStatus())
                .messageRequest(p.getRequestMessage())
                .messageResponse(p.getRequestMessage())
                .matchType(p.getMatch().getChallengeMatchType().getName())
                .canPay(p.getStatus().equals(EParticipantStatus.ACCEPTED) && !p.isPaid())
                .build()).toList();
    }

    @Override
    public Integer getAcceptedCurrentPlayers(Long matchId) {
        ChallengeMatch match = challengeMatchRepository.findById(matchId).orElseThrow(() -> new ResourceNotFoundException("Match not found"));

        return Math.toIntExact(match.getParticipants().stream().filter(p -> p.getStatus().equals(EParticipantStatus.ACCEPTED)).count());
    }

    @Override
    public Integer getPendingCurrentPlayers(Long matchId) {
        ChallengeMatch match = challengeMatchRepository.findById(matchId).orElseThrow(() -> new ResourceNotFoundException("Match not found"));

        return Math.toIntExact(match.getParticipants().stream().filter(p -> p.getStatus().equals(EParticipantStatus.PENDING)).count());
    }

    @Override
    public Integer countAcceptedPlayersByTeam(Long matchId, ETeam team) {
        return challengeParticipantRepository.countAcceptedPlayersByTeam(matchId, team);
    }

    @Override
    public List<ChallengeParticipant> getParticipantsByMatchAndTeamAndStatus(Long matchId, ETeam team, EParticipantStatus status) {
        return challengeParticipantRepository.findByMatchIdAndTeamAndStatus(matchId, team, status);
    }

    private ChallengeParticipant findParticipant(Long id) {
        return challengeParticipantRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy sự tham gia nào cả"));
    }


}
