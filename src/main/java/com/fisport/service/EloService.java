package com.fisport.service;

import com.fisport.model.UserSportElo;

import java.util.List;

public interface EloService {
    double expectedScore(int eloA, int eloB);

    Integer averageTeamElo(List<UserSportElo> team);

    Double actualScore(int scortTeamA, int scortTeamB);

    
}
