package com.fisport.service.impl;

import com.fisport.common.EPaymentMethod;
import com.fisport.common.ETransactionStatus;
import com.fisport.common.ETransactionType;
import com.fisport.dto.response.TransactionResponse;
import com.fisport.model.Transaction;
import com.fisport.repository.TransactionRepository;
import com.fisport.service.TransactionService;
import com.fisport.service.TransactionSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "TRANSACTION-SERVICE")
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;

    @Override
    public Page<TransactionResponse> getTransactions(BigDecimal amount, EPaymentMethod paymentMethod,
                                                     ETransactionType transactionType, ETransactionStatus transactionStatus,
                                                     LocalDateTime fromDate, LocalDateTime toDate,
                                                     Long walletId, int page, int size, String sortField, String sortDir) {
        int pageNumber = 0;
        if (page > 0) {
            pageNumber = page - 1;
        }

        sortField = (sortField != null) ? sortField : "createdAt";

        Sort.Direction direction = "desc".equals(sortDir) ? Sort.Direction.DESC : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(pageNumber, size, Sort.by(direction, sortField));

        Specification<Transaction> spec = TransactionSpecification.filterTransaction(amount, paymentMethod, transactionType, transactionStatus, fromDate, toDate, walletId);

        Page<Transaction> transactions = transactionRepository.findAll(spec, pageable);

        return transactions.map(transaction -> TransactionResponse.builder()
                .id(transaction.getId())
                .amount(transaction.getAmount())
                .createAt(transaction.getCreatedAt())
                .paymentMethod(transaction.getMethod())
                .transactionType(transaction.getType())
                .transactionStatus(transaction.getStatus())
                .build());
    }
}
