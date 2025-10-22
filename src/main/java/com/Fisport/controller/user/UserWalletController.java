package com.Fisport.controller.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@Controller
@RequestMapping("/user/wallet")
public class UserWalletController {

    @GetMapping
    public String showWallet(Model model) {
        return "user/wallet";
    }
}
