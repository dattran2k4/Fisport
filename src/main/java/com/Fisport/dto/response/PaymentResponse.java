package com.Fisport.dto.response;

import com.Fisport.common.EPaymentMethod;
import com.Fisport.common.EPaymentStatus;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
public class PaymentResponse {
    private EPaymentMethod method;
    private EPaymentStatus status;
    private LocalDateTime paymentAt;
}
