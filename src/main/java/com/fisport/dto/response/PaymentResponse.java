package com.fisport.dto.response;

import com.fisport.common.EPaymentMethod;
import com.fisport.common.EPaymentStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
@Getter
@Setter
public class PaymentResponse {
    private String transactionId;
    private BigDecimal amount;
    private EPaymentMethod method;
    private EPaymentStatus status;
    private LocalDateTime paymentAt;
}
