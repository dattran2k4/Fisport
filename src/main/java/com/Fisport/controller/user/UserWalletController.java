package com.Fisport.controller.user;

import com.Fisport.common.EPaymentMethod;
import com.Fisport.common.ETransactionStatus;
import com.Fisport.common.ETransactionType;
import com.Fisport.dto.response.TransactionResponse;
import com.Fisport.service.TransactionService;
import com.Fisport.service.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RequiredArgsConstructor
@Controller
@RequestMapping("/user/wallet")
public class UserWalletController {

    private final WalletService walletService;
    private final TransactionService transactionService;

    @GetMapping
    public String showWallet(@RequestParam(value = "status", required = false) String status, Model model, Principal principal,
                             @RequestParam(required = false) BigDecimal amount,
                             @RequestParam(required = false) EPaymentMethod paymentMethod,
                             @RequestParam(required = false) ETransactionType transactionType,
                             @RequestParam(required = false) ETransactionStatus transactionStatus,
                             @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fromDate,
                             @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime toDate,
                             @RequestParam(required = false) Long walletId,
                             @RequestParam(defaultValue = "1") int page,
                             @RequestParam(defaultValue = "10") int size,
                             @RequestParam(required = false) String sortField,
                             @RequestParam(defaultValue = "desc") String sortDir) {


        if (status != null) {
            if (status.equals("SUCCESS")) {
                model.addAttribute("message", "Nạp vào ví thành công!");
                model.addAttribute("type", "success");
            } else {
                model.addAttribute("message", "Nạp vào ví thất bại");
                model.addAttribute("type", "error");
            }
        }

        if (sortField != null) {
            Pattern pattern = Pattern.compile("^([a-zA-Z]+)_(asc|desc)$");
            Matcher matcher = pattern.matcher(sortField);

            if (matcher.matches()) {
                sortField = matcher.group(1);
                sortDir = matcher.group(2);
            }
        }


        model.addAttribute("types", List.of(ETransactionType.values()));
        model.addAttribute("status", List.of(ETransactionStatus.values()));
        model.addAttribute("paymentMethods", List.of(EPaymentMethod.values()));
        model.addAttribute("wallet", walletService.getWalletByUser(principal.getName()));
        model.addAttribute("methods", List.of(EPaymentMethod.PAYOS, EPaymentMethod.VNPAY, EPaymentMethod.MOMO, EPaymentMethod.ZALOPAY));
        Page<TransactionResponse> transactions = transactionService.getTransactions(amount, paymentMethod, transactionType,
                transactionStatus, fromDate, toDate,
                walletService.getWalletByUser(principal.getName()).getId(), page, size, sortField, sortDir);
        model.addAttribute("transactions", transactions);
        return "user/wallet";
    }
}
