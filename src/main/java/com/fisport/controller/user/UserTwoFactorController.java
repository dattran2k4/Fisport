package com.fisport.controller.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@Controller
@RequestMapping("/user/2fa")
public class UserTwoFactorController {

    @GetMapping()
    public String showPage() {
        return "user/2fa";
    }
}
