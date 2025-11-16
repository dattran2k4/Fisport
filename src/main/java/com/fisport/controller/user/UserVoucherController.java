package com.fisport.controller.user;

import com.fisport.service.VoucherService;
import lombok.RequiredArgsConstructor;
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
    public String showVouchersUser(Model model, Principal principal) {
        model.addAttribute("vouchers", voucherService.getVouchersByUserId(principal.getName()));
        return "user/vouchers";
    }
}
