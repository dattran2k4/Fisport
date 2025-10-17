package com.Fisport.controller.user;

import com.Fisport.security.CustomUserDetails;
import com.Fisport.service.VoucherService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;


@RequiredArgsConstructor
@Controller
@RequestMapping("/user/vouchers")
public class UserVoucherController {

    private final VoucherService voucherService;

    @GetMapping()
    public String showVouchersUser(Model model, @AuthenticationPrincipal CustomUserDetails principal) {
        model.addAttribute("vouchers", voucherService.getVouchersByUserId(principal.getUser().getId()));
        return "user/vouchers";
    }
}
