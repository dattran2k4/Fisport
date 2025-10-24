package com.Fisport.service.impl;

import com.Fisport.common.EPaymentMethod;
import com.Fisport.common.ETransactionStatus;
import com.Fisport.common.ETransactionType;
import com.Fisport.dto.response.TransactionResponse;
import com.Fisport.model.Transaction;
import com.Fisport.repository.TransactionRepository;
import com.Fisport.service.TransactionService;
import com.Fisport.service.TransactionSpecification;
import lombok.RequiredArgsConstructor;
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
