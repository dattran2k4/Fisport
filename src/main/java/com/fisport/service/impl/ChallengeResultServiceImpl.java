package com.fisport.service.impl;

import com.fisport.common.ETeam;
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
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChallengeResultServiceImpl implements ChallengeResultService {

    private final ChallengeMatchService challengeMatchService;
    private final ChallengeParticipantService challengeParticipantService;
    private final ChallengeMatchRepository challengeMatchRepository;
    private final ChallengeResultRepository challengeResultRepository;
    private final UserSportEloService userSportEloService;

    @Override
    @Transactional
    public void updateMatchResult(Long matchID, Integer scortTeamA, Integer scortTeamB) {

        ChallengeMatch match = challengeMatchRepository.findById(matchID).orElseThrow(()-> new ResourceNotFoundException("Match not found"));

        challengeMatchService.checkMatchFinished(matchID);

        ChallengeResult result = ChallengeResult.builder()
                .match(match)
                .teamAScort(scortTeamA)
                .teamBScort(scortTeamB)
                .build();

        challengeResultRepository.save(result);

        log.info("challengeId updated result {} - {}", match, scortTeamA, scortTeamB);


        //Get list participants in team
        List<ChallengeParticipant> participantsA = challengeParticipantService.getParticipantsByMatchAndTeam(matchID, ETeam.TEAM_A);
        List<ChallengeParticipant> participantsB = challengeParticipantService.getParticipantsByMatchAndTeam(matchID, ETeam.TEAM_B);

        if (participantsA.isEmpty() && participantsB.isEmpty()) return;

        //Get Ids
        List<Long> userIdsTeamA = participantsA.stream().map(c -> c.getUser().getId()).toList();
        List<Long> userIdsTeamB = participantsB.stream().map(c -> c.getUser().getId()).toList();

        //Get list elo Team
        List<UserSportElo> eloTeamA = userSportEloService.getUserSportEloByUserIds(userIdsTeamA);
        List<UserSportElo> eloTeamB = userSportEloService.getUserSportEloByUserIds(userIdsTeamB);

        //Save
        userSportEloService.updateSportElo(eloTeamA, eloTeamB, scortTeamA, scortTeamB);
        log.info("challengeId updated result {} - {}", match, scortTeamA, scortTeamB);

    }
}
