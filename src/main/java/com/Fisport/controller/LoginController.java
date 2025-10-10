package com.Fisport.controller;

import com.Fisport.dto.request.LoginRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@Controller
@RequestMapping
public class LoginController {

    private AuthService

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("request", new LoginRequestDTO());
        return "login";
    }

    @Post
}
