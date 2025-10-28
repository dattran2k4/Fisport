package com.fisport.service.impl;

import com.fisport.model.UserSportElo;
import com.fisport.service.EloService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EloServiceImpl implements EloService {

    @Override
    public double expectedScore(int eloA, int eloB) {
        return 1.0 / (1 + Math.pow(10, (eloB - eloA) / 400.0));
    }

    @Override
    public Integer averageTeamElo(List<UserSportElo> team) {
        return null;
    }

    @Override
    public Double actualScore(int scortTeamA, int scortTeamB) {
        return 0.0;
    }
}
