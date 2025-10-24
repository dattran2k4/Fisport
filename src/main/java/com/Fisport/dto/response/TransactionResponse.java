package com.Fisport.dto.response;

import com.Fisport.common.EPaymentMethod;
import com.Fisport.common.ETransactionStatus;
import com.Fisport.common.ETransactionType;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Builder
public class TransactionResponse {
    private Long id;
    private BigDecimal amount;
    private EPaymentMethod paymentMethod;
    private ETransactionType transactionType;
    private ETransactionStatus transactionStatus;
    private LocalDateTime createAt;
}
