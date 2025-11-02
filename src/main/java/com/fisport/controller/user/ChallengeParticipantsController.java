package com.fisport.controller.user;

import com.fisport.service.ChallengeParticipantService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@RequiredArgsConstructor
@Controller
@RequestMapping("user/participants")
public class ChallengeParticipantsController {

    private final ChallengeParticipantService challengeParticipantService;

    @GetMapping
    public String participants(Model model, Principal principal){
        model.addAttribute("matches", challengeParticipantService.getAllPariticipantsByUser(principal.getName()));
        return "user/challenge-participants";
    }
}
