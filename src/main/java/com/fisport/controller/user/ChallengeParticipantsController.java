package com.fisport.controller.user;

import com.fisport.dto.response.ChallengeParticipantsForUserResponse;
import com.fisport.dto.response.ChallengeResultResponse;
import com.fisport.service.ChallengeParticipantService;
import com.fisport.service.ChallengeResultService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("user/participants")
public class ChallengeParticipantsController {

    private final ChallengeParticipantService challengeParticipantService;
    private final ChallengeResultService challengeResultService;

    @GetMapping
    public String participants(Model model, Principal principal){
        List<ChallengeParticipantsForUserResponse> list = challengeParticipantService.getAllPariticipantsByUser(principal.getName());

        list.forEach(p -> {
            ChallengeResultResponse result = challengeResultService.getByMatchId(p.getMatchId());
            p.setResult(result);
        });

        model.addAttribute("matches", list);
        return "user/challenge-participants";
    }
}
