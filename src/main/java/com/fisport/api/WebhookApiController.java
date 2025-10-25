package com.fisport.api;

import com.fisport.dto.response.ApiResponse;
import com.fisport.service.WalletPaymentService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import vn.payos.PayOS;
import vn.payos.model.webhooks.WebhookData;

import java.util.Map;

@RestController
@RequestMapping("/api/webhook")
@RequiredArgsConstructor
@Slf4j
@Validated
public class WebhookApiController {

    private final WalletPaymentService walletPaymentService;
    private final PayOS payOS;

    @GetMapping("/vnpay-ipn")
    public ApiResponse handleVnPayReturn(@RequestParam Map<String, String> params) {

        log.info("VNPAY return received: {}", params);

        walletPaymentService.handleVnPayReturn(params);
        return ApiResponse.builder()
                .status(200)
                .message("VnPay returned")
                .build();
    }

    @PostMapping("/payos-return")
    public ApiResponse showPaymentStatus(@RequestBody(required = false) Map<String, Object> body) {
        log.info("PayOS return received: {}", body);
        WebhookData data = payOS.webhooks().verify(body);
        walletPaymentService.handlePayOSReturn(data);

        return ApiResponse.builder()
                .status(HttpStatus.CREATED.value())
                .message("PayOS returned")
                .build();
    }

}
