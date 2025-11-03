package com.fisport.controller.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@Controller
@RequestMapping("change-password")
public class ChangePasswordController {

    @GetMapping
    public String changePassword(){
        return "change-password";
    }
}
