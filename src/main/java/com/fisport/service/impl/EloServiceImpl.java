package com.fisport.service.impl;

import com.fisport.model.UserSportElo;
import com.fisport.service.EloService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EloServiceImpl implements EloService {

    private final int K = 32;
    //E new = E old + K (S - E)
    // S: actual score
    //E: expectedScore

    @Override
    public double expectedScore(int eloA, int eloB) {
        return 1.0 / (1 + Math.pow(10, (eloB - eloA) / 400.0));
    }

    @Override
    public Integer averageTeamElo(List<UserSportElo> team) {
        return (int) team.stream().mapToInt(UserSportElo::getElo).average().orElse(1200);
    }

    @Override
    public Double actualScore(int scoreTeamA, int scoreTeamB) {
        if (scoreTeamA > scoreTeamB) return 1.0;
        if (scoreTeamA < scoreTeamB) return 0.0;
        return 0.5;
    }

    @Override
    public void updateTeamElo(List<UserSportElo> teamA, List<UserSportElo> teamB, int scoreA, int scoreB) {

        int avgEloTeamA =  averageTeamElo(teamA);
        int avgEloTeamB = averageTeamElo(teamB);

        double expectedScoreTeamA = expectedScore(avgEloTeamA, avgEloTeamB);
        double expectedScoreTeamB = expectedScore(avgEloTeamB, avgEloTeamA);

        double actualA = actualScore(scoreA, scoreB);
        double actualB = actualScore(scoreB, scoreA);

        int dentaA = (int) (K * (actualA - expectedScoreTeamA));
        int dentaB = (int) (K * (actualB - expectedScoreTeamB));

        teamA.forEach(userSportElo -> {userSportElo.setElo(userSportElo.getElo() + dentaA);});
        teamB.forEach(userSportElo -> userSportElo.setElo(userSportElo.getElo() + dentaB));
    }

}
