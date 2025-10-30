package com.fisport.controller;

import com.fisport.service.ChallengeMatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@Controller
@RequestMapping("/thach-dau")
public class ChallengeMatchController {

    private final ChallengeMatchService challengeMatchService;

    @GetMapping
    public String challengeMatch(Model model) {

        return "web/challenge-matchs";
    }
}
