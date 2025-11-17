package com.fisport.service.impl;

import com.fisport.common.ELevel;
import com.fisport.dto.response.UserSportEloResponse;
import com.fisport.exception.ResourceNotFoundException;
import com.fisport.model.UserSportElo;
import com.fisport.repository.UserSportEloRepository;
import com.fisport.service.EloService;
import com.fisport.service.UserSportEloService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "USER-SPORT-ELO-SERVICE")
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
        log.info("teamA size: {}", teamA.size());
        log.info("teamB size: {}", teamB.size());

        eloService.updateTeamElo(teamA, teamB, scoreA, scoreB);

        teamA.forEach(t -> t.setLevel(ELevel.fromElo(t.getElo())));
        teamB.forEach(t -> t.setLevel(ELevel.fromElo(t.getElo())));

        userSportEloRepository.saveAll(teamA);
        userSportEloRepository.saveAll(teamB);
    }


    @Override
    public List<UserSportElo> getUserSportEloByUserIds(List<Long> userIds, Long sportId) {
        return userSportEloRepository.findByUserIdAndSport(userIds, sportId);
    }

    @Override
    public UserSportEloResponse getUserSportEloResponse(Long userId, Long sportId) {
        UserSportElo userSportElo = getUserSportElo(userId, sportId);
        return UserSportEloResponse.builder().elo(userSportElo.getElo()).level(userSportElo.getLevel()).build();
    }

    private UserSportElo getUserSportElo(Long userId, Long sportId) {
        return userSportEloRepository.findByUserIdAndFieldTypeId(userId, sportId).orElseThrow(() -> new ResourceNotFoundException("User Sport Elo Not Found"));
    }
}
