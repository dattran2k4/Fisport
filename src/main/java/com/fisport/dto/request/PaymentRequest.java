package com.fisport.dto.request;

import com.fisport.common.EPaymentMethod;
import com.fisport.dto.validator.EnumValue;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class PaymentRequest {
    @NotBlank(message = "Chưa có token booking")
    private String paymentToken;

    @EnumValue(name = "paymentMethod", enumClass = EPaymentMethod.class)
    private EPaymentMethod paymentMethod;
}
