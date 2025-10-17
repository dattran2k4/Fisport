package com.Fisport.controller.user;

import com.Fisport.service.UserDashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@RequiredArgsConstructor
@Controller
@RequestMapping("/user")
public class UserDashboardController {

    private final UserDashboardService userDashboardService;

    @GetMapping()
    public String showDashboard(Model model, Principal principal) {
        model.addAttribute("response", userDashboardService.getData(principal.getName()));
        return "user/dashboard";
    }
}
