package com.fisport.service.impl;

import com.fisport.common.EChallengeStatus;
import com.fisport.common.EParticipantStatus;
import com.fisport.common.ETeam;
import com.fisport.dto.request.MatchResultRequest;
import com.fisport.dto.response.ChallengeResultResponse;
import com.fisport.exception.InvalidDataException;
import com.fisport.exception.ResourceNotFoundException;
import com.fisport.model.ChallengeMatch;
import com.fisport.model.ChallengeParticipant;
import com.fisport.model.ChallengeResult;
import com.fisport.model.UserSportElo;
import com.fisport.repository.ChallengeMatchRepository;
import com.fisport.repository.ChallengeResultRepository;
import com.fisport.service.ChallengeParticipantService;
import com.fisport.service.ChallengeResultService;
import com.fisport.service.ChallengeMatchService;
import com.fisport.service.UserSportEloService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "CHALLENGE-RESULT-SERVICE")
public class ChallengeResultServiceImpl implements ChallengeResultService {

    private final ChallengeMatchService challengeMatchService;
    private final ChallengeParticipantService challengeParticipantService;
    private final ChallengeMatchRepository challengeMatchRepository;
    private final ChallengeResultRepository challengeResultRepository;
    private final UserSportEloService userSportEloService;

    @Override
    @Transactional(rollbackFor =  Exception.class)
    public void updateMatchResult(Long matchID, MatchResultRequest request, String username) {

        log.info("Updating result for matchId {}", matchID);

        ChallengeMatch match = challengeMatchRepository.findById(matchID).orElseThrow(() -> new ResourceNotFoundException("Match not found"));

        if (!match.getCreator().getUsername().equals(username)) {
            throw new InvalidDataException("Chỉ có chủ trận mới được thực hiện");
        }

        if (match.getResult() != null) {
            throw new InvalidDataException("Không thể cập nhật lại kết quả");
        }

        //TO-DO UPDATE MATCHED REAL TIME

        if (!match.getStatus().equals(EChallengeStatus.MATCHED)) {
            throw new InvalidDataException("Trận đấu chưa hoàn thành");
        }

        ChallengeResult result = match.getResult();


        if (result == null) {
            result = new ChallengeResult();
            result.setMatch(match);
            result.setTeamAScort(request.getScortTeamA());
            result.setTeamBScort(request.getScortTeamB());
            challengeResultRepository.save(result);
            log.info("MatchId {} updated result {} - {}", matchID, request.getScortTeamA(), request.getScortTeamB());
        } else {
            throw new InvalidDataException("Kết quả trận đấu đã được cập nhật. Không thể chỉnh sửa!");
        }

        //Get list participants in team
        List<ChallengeParticipant> participantsA = challengeParticipantService.getParticipantsByMatchAndTeamAndStatus(matchID, ETeam.TEAM_A, EParticipantStatus.ACCEPTED);
        log.info("Get participantsA: {}", participantsA.toString());
        List<ChallengeParticipant> participantsB = challengeParticipantService.getParticipantsByMatchAndTeamAndStatus(matchID, ETeam.TEAM_B, EParticipantStatus.ACCEPTED);
        log.info("Get participantsB: {}", participantsB.toString());

        if (participantsA.isEmpty() && participantsB.isEmpty()) {
            throw new InvalidDataException("Không thể cập nhật kết quả");
        }

        //Get Ids
        List<Long> userIdsTeamA = participantsA.stream().map(c -> c.getUser().getId()).toList();
        List<Long> userIdsTeamB = participantsB.stream().map(c -> c.getUser().getId()).toList();

        //Get list elo Team
        List<UserSportElo> eloTeamA = userSportEloService.getUserSportEloByUserIds(userIdsTeamA, match.getFieldTypeId());
        log.info("eloTeamA: {}", eloTeamA.toString());
        List<UserSportElo> eloTeamB = userSportEloService.getUserSportEloByUserIds(userIdsTeamB, match.getFieldTypeId());
        log.info("eloTeamB: {}", eloTeamB.toString());

        //Save
        userSportEloService.updateSportElo(eloTeamA, eloTeamB, request.getScortTeamA(), request.getScortTeamB());
    }

    @Override
    public List<ChallengeResultResponse> getAll(Long matchId, String username) {
        return null;
    }

    @Override
    public ChallengeResultResponse getByMatchId(Long matchId) {
        log.info("get result by matchId {}", matchId);
        ChallengeMatch match =  challengeMatchRepository.findById(matchId).orElseThrow(() -> new ResourceNotFoundException("Match not found"));

        ChallengeResult result = match.getResult();

        if (result == null) {
            return ChallengeResultResponse.builder()
                    .matchId(matchId)
                    .hasResult(false)
                    .build();
        }

        return ChallengeResultResponse.builder()
                .matchId(matchId)
                .scoreTeamA(result.getTeamAScort())
                .scoreTeamB(result.getTeamBScort())
                .hasResult(true)
                .build();
    }
}
