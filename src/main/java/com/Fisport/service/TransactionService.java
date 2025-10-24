package com.Fisport.service;

import com.Fisport.common.EPaymentMethod;
import com.Fisport.common.ETransactionStatus;
import com.Fisport.common.ETransactionType;
import com.Fisport.dto.response.TransactionResponse;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface TransactionService {

    Page<TransactionResponse> getTransactions(BigDecimal amount,
                                              EPaymentMethod paymentMethod,
                                              ETransactionType transactionType,
                                              ETransactionStatus transactionStatus,
                                              LocalDateTime fromDate, LocalDateTime toDate,
                                              Long walletId, int page, int size, String sortField, String sortDir);
}
