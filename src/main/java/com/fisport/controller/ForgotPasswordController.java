package com.fisport.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequiredArgsConstructor
@Controller
@RequestMapping("")
public class ForgotPasswordController {

    @GetMapping("/forgot-password")
    public String showforgotPasswordPage() {
        return "forgot-password";
    }

    @GetMapping("/reset-password")
    public String showresetPasswordPage(@RequestParam("verifyCode") String verifyCode,
                                Model model) {
        model.addAttribute("verifyCode", verifyCode);
        return "reset-password";
    }
}
