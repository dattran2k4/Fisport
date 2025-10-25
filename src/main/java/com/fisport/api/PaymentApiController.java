package com.fisport.api;

import com.fisport.dto.request.PaymentRequest;
import com.fisport.dto.request.WalletTopUpRequest;
import com.fisport.dto.response.ApiResponse;
import com.fisport.service.PaymentService;
import com.fisport.service.WalletPaymentService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import vn.payos.PayOS;
import vn.payos.model.webhooks.WebhookData;

import java.security.Principal;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@Validated
@RequestMapping("/api/payment")
public class PaymentApiController {

    private final PaymentService paymentService;
    private final PayOS payOS;
    private final WalletPaymentService walletPaymentService;

    @PostMapping("/create-payment")
    public ApiResponse createPaymentUrl(@Valid @RequestBody PaymentRequest request, HttpServletRequest httpServletRequest) {
        String paymentUrl = paymentService.createPayment(request, httpServletRequest);
        return ApiResponse.builder()
                .status(HttpStatus.CREATED.value())
                .message("Payment created successfully")
                .data(paymentUrl)
                .build();
    }

//    @PostMapping("/payos-create")
//    public ApiResponse<?> createPayOSUrl() {
//        String paymentUrl = payOSService.createPaymentLink();
//        return ApiResponse.builder()
//                .status(HttpStatus.CREATED.value())
//                .message("PayOS created successfully")
//                .data(paymentUrl)
//                .build();
//    }

    @PostMapping("/webhook/payos")
    public ApiResponse showPaymentStatus(@RequestBody(required = false) Map<String, Object> body) {
        WebhookData data = payOS.webhooks().verify(body);
        System.out.println("PayOS data: " + data);
        paymentService.handlePayOSWebHook(data);

        return ApiResponse.builder()
                .status(HttpStatus.CREATED.value())
                .message("PayOS webhook has been send and saved successfully")
                .build();
    }

    @PostMapping("/create-payment-wallet-topup")
    public ApiResponse createWalletPaymentUrl(@Valid @RequestBody WalletTopUpRequest request, HttpServletRequest httpServletRequest) {
        String paymentUrl = paymentService.createWalletPayment(request, httpServletRequest);
        return ApiResponse.builder()
                .status(HttpStatus.CREATED.value())
                .message("Payment created")
                .data(paymentUrl)
                .build();
    }

    @PostMapping("/refund-booking/{bookingId}")
    public ApiResponse refundBooking(@PathVariable Long bookingId) {
        walletPaymentService.refundBooking(bookingId);

        return ApiResponse.builder()
                .status(200)
                .message("Refunded booking")
                .build();
    }

    @PostMapping("/booking-wallet-pay")
    public ApiResponse payBookingWallet(@RequestParam String paymentToken, Principal principal) {
        walletPaymentService.payBooking(paymentToken, principal.getName());

        return ApiResponse.builder()
                .status(200)
                .message("Payment successfully")
                .build();
    }
}
