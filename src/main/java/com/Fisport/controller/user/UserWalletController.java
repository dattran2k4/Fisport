package com.Fisport.controller.user;

import com.Fisport.common.EPaymentMethod;
import com.Fisport.service.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/user/wallet")
public class UserWalletController {

    private final WalletService walletService;

    @GetMapping
    public String showWallet(@RequestParam(value = "status", required = false) String status, Model model, Principal principal) {


        if (status != null) {
            if (status.equals("SUCCESS")) {
                model.addAttribute("message", "Nạp vào ví thành công!");
                model.addAttribute("type", "success");
            } else {
                model.addAttribute("message", "Nạp vào ví thất bại");
                model.addAttribute("type", "error");
            }
        }

        model.addAttribute("balance", walletService.getBalanceByUser(principal.getName()));
        model.addAttribute("methods", List.of(EPaymentMethod.PAYOS, EPaymentMethod.VNPAY, EPaymentMethod.MOMO, EPaymentMethod.ZALOPAY));

        return "user/wallet";
    }
}
