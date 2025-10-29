package com.fisport.service.impl;

import com.fisport.common.ELevel;
import com.fisport.model.UserSportElo;
import com.fisport.repository.UserSportEloRepository;
import com.fisport.service.EloService;
import com.fisport.service.UserSportEloService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserSportEloServiceImpl implements UserSportEloService {

    private final UserSportEloRepository userSportEloRepository;
    private final EloService eloService;

    @Override
    public int getElo(Long userId, Long sportId) {
        return getUserSportElo(userId, sportId).getElo();
    }

    @Override
    public ELevel getLevel(Long userId, Long sportId) {
        return getUserSportElo(userId, sportId).getLevel();
    }

    @Override
    public void updateSportElo(List<UserSportElo> teamA, List<UserSportElo> teamB, int scoreA, int scoreB) {
        eloService.updateTeamElo(teamA, teamB, scoreA, scoreB);
        userSportEloRepository.saveAll(teamA);
        userSportEloRepository.saveAll(teamB);
    }

    @Override
    public void updateSportLevel(Long userId, Integer elo) {
        
    }

    @Override
    public List<UserSportElo> getUserSportEloByUserIds(List<Long> userIds) {
        return userSportEloRepository.findByUserIdIn(userIds);
    }

    private UserSportElo getUserSportElo(Long userId, Long sportId) {
        return userSportEloRepository.findByUserIdAndSportId(userId, sportId).orElse(null);
    }
}
