package com.Fisport.dto.request;

import com.Fisport.common.EPaymentMethod;
import com.Fisport.dto.validator.EnumValue;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class WalletTopUpRequest {
    @Min(value = 10000, message = "Nạp tối thiểu 10000 đồng")
    private BigDecimal amount;

    @EnumValue(name = "paymentMethod", enumClass = EPaymentMethod.class)
    private EPaymentMethod paymentMethod;

    @NotNull(message = "Ví đâu")
    private Long walletId;
}
