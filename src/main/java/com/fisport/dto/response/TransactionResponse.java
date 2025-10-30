package com.fisport.dto.response;

import com.fisport.common.EPaymentMethod;
import com.fisport.common.ETransactionStatus;
import com.fisport.common.ETransactionType;
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
