package com.fisport.controller;

import com.fisport.common.EChallengeStatus;
import com.fisport.common.ELevel;
import com.fisport.common.ETeam;
import com.fisport.model.ChallengeMatch;
import com.fisport.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/thach-dau")
public class ChallengeMatchController {

    private final ChallengeMatchService challengeMatchService;
    private final ChallengeMatchTypeService challengeMatchTypeService;
    private final ChallengeParticipantService challengeParticipantService;
    private final UserSportEloService userSportEloService;
    private final CityService cityService;
    private final FieldTypeService fieldTypeService;

    @GetMapping
    public String challengeMatch(Model model, @RequestParam(required = false) EChallengeStatus status,
                                 @RequestParam(required = false) ELevel level,
                                 @RequestParam(required = false) Long typeId,
                                 @RequestParam(required = false) Long cityId,
                                 @RequestParam(required = false) Long fieldTypeId,
                                 @RequestParam(defaultValue = "0") int page,
                                 @RequestParam(defaultValue = "10") int size) {

        model.addAttribute("cities", cityService.findAll());
        model.addAttribute("status", List.of(EChallengeStatus.OPEN, EChallengeStatus.FULL, EChallengeStatus.PENDING));
        model.addAttribute("level", List.of(ELevel.values()));
        model.addAttribute("sports", fieldTypeService.findAll());
        model.addAttribute("matchTypes", challengeMatchTypeService.getAll());

        model.addAttribute("matches", challengeMatchService.getAllChallengeMatch(status, level, typeId, cityId, fieldTypeId, page, size));
        return "web/challenge-matchs";
    }

    @GetMapping("/{id}/chi-tiet")
    public String challengeMatchDetail(@PathVariable Long id, Model model) {
        ChallengeMatch match = challengeMatchService.findChallengeMatch(id);

        int maxPlayersPerTeam = match.getChallengeMatchType().getMaxPlayers() / 2;
        model.addAttribute("maxPlayersPerTeam", maxPlayersPerTeam);

        model.addAttribute("creator", userSportEloService.getUserSportEloResponse(match.getCreator().getId(), match.getFieldTypeId()));

        model.addAttribute("teams", ETeam.values());

        model.addAttribute("match", challengeMatchService.getChallengeMatchDetail(id));

        model.addAttribute("participants", challengeParticipantService.getAllAcceptedParticipantsInfo(id));

        model.addAttribute("teamA", ETeam.TEAM_A);

        model.addAttribute("teamB", ETeam.TEAM_B);

        model.addAttribute("teamAPlayer", challengeParticipantService.countAcceptedPlayersByTeam(id, ETeam.TEAM_A));

        model.addAttribute("teamBPlayer", challengeParticipantService.countAcceptedPlayersByTeam(id, ETeam.TEAM_B));

        return "web/challenge-match-detail";
    }
}
