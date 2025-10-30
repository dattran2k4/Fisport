package com.fisport.service;

import com.fisport.common.EPaymentMethod;
import com.fisport.common.ETransactionStatus;
import com.fisport.common.ETransactionType;
import com.fisport.dto.response.TransactionResponse;
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
