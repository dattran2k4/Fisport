package com.fisport.service.impl;

import com.fisport.common.ELevel;
import com.fisport.model.UserSportElo;
import com.fisport.repository.UserSportEloRepository;
import com.fisport.service.UserSportEloService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserSportEloServiceImpl implements UserSportEloService {

    private final UserSportEloRepository userSportEloRepository;

    @Override
    public int getElo(Long userId, Long sportId) {
        return getUserSportElo(userId, sportId).getElo();
    }

    @Override
    public ELevel getLevel(Long userId, Long sportId) {
        return getUserSportElo(userId, sportId).getLevel();
    }

    @Override
    public void updateSportElo(Long userId, Long sportId, Integer newElo) {
        UserSportElo sportElo = userSportEloRepository.findByUserIdAndSportId(userId, sportId).orElse(null);
        sportElo.setElo(newElo);
        userSportEloRepository.save(sportElo);
    }

    @Override
    public void updateSportLevel(Long userId, Integer elo) {
        
    }

    private UserSportElo getUserSportElo(Long userId, Long sportId) {
        return userSportEloRepository.findByUserIdAndSportId(userId, sportId).orElse(null);
    }
}
