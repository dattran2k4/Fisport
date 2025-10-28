package com.fisport.service.impl;

import com.fisport.exception.InvalidDataException;
import com.fisport.model.ChallengeMatch;
import com.fisport.model.ChallengeParticipant;
import com.fisport.model.ChallengeResult;
import com.fisport.repository.ChallengeMatchRepository;
import com.fisport.repository.ChallengeResultRepository;
import com.fisport.service.ChallengeResultService;
import com.fisport.service.ChallengeMatchService;
import com.fisport.service.UserSportEloService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChallengeResultServiceImpl implements ChallengeResultService {

    private final ChallengeMatchService challengeMatchService;
    private final ChallengeMatchRepository challengeMatchRepository;
    private final ChallengeResultRepository challengeResultRepository;
    private final UserSportEloService userSportEloService;

    @Override
    @Transactional
    public void updateMatchResult(Long matchID, Integer scortTeamA, Integer scortTeamB) {

        ChallengeMatch match = challengeMatchRepository.findById(matchID).orElse(null);

        challengeMatchService.checkMatchFinished(matchID);

        ChallengeResult result = ChallengeResult.builder()
                .match(match)
                .teamAScort(scortTeamA)
                .teamBScort(scortTeamB)
                .build();

        challengeResultRepository.save(result);

        log.info("challengeId updated result {} - {}", match, scortTeamA, scortTeamB);


        //lay-list-pariticipants-team

        //cap-nhat-dung-user-sport-elo-service
    }
}
