package com.Fisport.dto.request;

import com.Fisport.common.EPaymentMethod;
import com.Fisport.dto.validator.EnumValue;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class WalletTopUpRequest {
    @Min(1)
    private BigDecimal amount;

    @EnumValue(name = "paymentMethod", enumClass = EPaymentMethod.class)
    private EPaymentMethod paymentMethod;

    @NotNull
    private Long walletId;
}
