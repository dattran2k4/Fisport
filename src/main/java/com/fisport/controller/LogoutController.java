package com.fisport.controller;

import com.fisport.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@Controller
@RequestMapping("/logout")
public class LogoutController {

    private final AuthService authService;

    @GetMapping()
    public String logout(HttpServletRequest request) {
        authService.logout();
        request.getSession().invalidate();
        return "redirect:/login";
    }
}
