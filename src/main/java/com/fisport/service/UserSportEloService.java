package com.fisport.service;

import com.fisport.common.ELevel;
import com.fisport.dto.response.UserSportEloResponse;
import com.fisport.model.UserSportElo;

import java.util.List;

public interface UserSportEloService {
    int getElo(Long userId, Long sportId);

    ELevel getLevel(Long userId, Long sportId);

    void updateSportElo(List<UserSportElo> teamA, List<UserSportElo> teamB, int scoreA, int scoreB);

    List<UserSportElo> getUserSportEloByUserIds(List<Long> ids, Long sportId);

    UserSportEloResponse getUserSportEloResponse(Long userId, Long sportId);
}
