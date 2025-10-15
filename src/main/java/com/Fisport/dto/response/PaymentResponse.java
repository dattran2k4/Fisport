package com.Fisport.dto.response;

import com.Fisport.common.EPaymentMethod;
import com.Fisport.common.EPaymentStatus;
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
