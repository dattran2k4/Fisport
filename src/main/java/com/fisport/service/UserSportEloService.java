package com.fisport.service;

import com.fisport.common.ELevel;
import com.fisport.model.UserSportElo;

import java.util.List;

public interface UserSportEloService {
    int getElo(Long userId, Long sportId);

    ELevel getLevel(Long userId, Long sportId);

    void updateSportElo(List<UserSportElo> teamA, List<UserSportElo> teamB, int scoreA, int scoreB);

    void updateSportLevel(Long userId, Integer elo);

    List<UserSportElo> getUserSportEloByUserIds(List<Long> ids);
}
