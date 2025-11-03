package com.fisport.controller.user;

import com.fisport.dto.response.ChallengeResultResponse;
import com.fisport.service.ChallengeMatchService;
import com.fisport.service.ChallengeParticipantService;
import com.fisport.service.ChallengeResultService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@RequiredArgsConstructor
@Controller
@RequestMapping("/user/challenge-matches")
public class UserChallengeMatchController {

    private final ChallengeMatchService challengeMatchService;
    private final ChallengeParticipantService challengeParticipantService;
    private final ChallengeResultService challengeResultService;

    @GetMapping
    public String showChallengeMatchManagement(Model model, Principal principal) {
        model.addAttribute("matches", challengeMatchService.getListMatchForManagement(principal.getName()));

        return "user/challenge-matches";
    }

    @GetMapping("/{id}")
    public String viewChallengeMatch( @PathVariable Long id, Model model, Principal principal) {
        model.addAttribute("match", challengeMatchService.getMatchDetailForManagement(id, principal.getName()));
        model.addAttribute("participants", challengeParticipantService.getAllParticipantsByMatchAndCreator(id, principal.getName()));
        return "user/challenge-match-detail";
    }
}
