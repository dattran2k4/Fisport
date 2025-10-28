package com.fisport.service;

import com.fisport.common.ELevel;

public interface UserSportEloService {
    int getElo(Long userId, Long sportId);

    ELevel getLevel(Long userId, Long sportId);

    void updateSportElo(Long userId, Long sportId, Integer newElo);

    void updateSportLevel(Long userId, Integer elo);
}
