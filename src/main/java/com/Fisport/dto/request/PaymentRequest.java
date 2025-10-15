package com.Fisport.dto.request;

import com.Fisport.common.EPaymentMethod;
import com.Fisport.dto.validator.EnumValue;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class PaymentRequest {
    @NotBlank(message = "Chưa có token booking")
    private String paymentToken;

    @EnumValue(name = "paymentMethod", enumClass = EPaymentMethod.class)
    private EPaymentMethod paymentMethod;
}
