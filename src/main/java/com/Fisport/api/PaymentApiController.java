package com.Fisport.api;

import com.Fisport.dto.request.PaymentRequest;
import com.Fisport.dto.response.ApiResponse;
import com.Fisport.model.Payment;
import com.Fisport.service.PaymentService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@Validated
@RequestMapping("/api/payment")
public class PaymentApiController {

    private final PaymentService paymentService;

    @PostMapping("/create-payment")
    public ApiResponse<?> createPaymentUrl(@Valid @RequestBody PaymentRequest request, HttpServletRequest httpServletRequest) {
        String paymentUrl = paymentService.createPayment(request, httpServletRequest);
        return ApiResponse.builder()
                .status(HttpStatus.CREATED.value())
                .message("Payment created successfully")
                .data(paymentUrl)
                .build();
    }
}
