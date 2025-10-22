package com.Fisport.controller;

import com.Fisport.common.EPaymentMethod;
import com.Fisport.common.EPaymentStatus;
import com.Fisport.dto.response.PaymentResponse;
import com.Fisport.service.PaymentService;
import com.Fisport.service.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.Map;

@RequiredArgsConstructor
@Controller
@RequestMapping
public class PaymentController {

    private final PaymentService paymentService;
    private final WalletService walletService;

    @GetMapping("/thanh-toan")
    public String showMethodPayment(@RequestParam("token") String paymentToken, Model model, Principal principal) {
        model.addAttribute("paymentToken", paymentToken);
        model.addAttribute("methods", EPaymentMethod.values());
        model.addAttribute("balance", walletService.getBalanceByUser(principal.getName()));
        return "web/payment";
    }

    @GetMapping("/vnpay-return")
    public String showPaymentStatus(@RequestParam Map<String, String> params, Model model) {
        PaymentResponse response = paymentService.handleVnpayReturn(params);
        model.addAttribute("response", response);
        if (response.getStatus().equals(EPaymentStatus.FAILED)) {
            return "payment-fail";
        }
        return "payment-success";
    }

    @GetMapping("/thanh-toan/payos/thanh-cong")
    public String showPaymentPayOSSucces(@RequestParam("orderCode") long orderCode, @RequestParam("status") String status, Model model) {
        PaymentResponse response = paymentService.checkPaymentPayOSView(orderCode, status);
        model.addAttribute("response", response);
        return "payment-success";
    }

    @GetMapping("/thanh-toan/payos/that-bai")
    public String showPaymentPayOSFail(@RequestParam("orderCode") long orderCode, @RequestParam("status") String status, Model model) {
        PaymentResponse response = paymentService.checkPaymentPayOSView(orderCode, status);
        model.addAttribute("response", response);
        return "payment-fail";
    }
}
