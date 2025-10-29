package com.fisport.controller.user;

import com.fisport.service.ChallengeMatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@Controller
@RequestMapping("/user")
public class UserChallengeMatchController {

    private final ChallengeMatchService challengeMatchService;

    public String showChallengeMatchManagement(Model model) {

        model.addAttribute("matchs");
        return null;
    }
}
