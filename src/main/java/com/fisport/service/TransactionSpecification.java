package com.fisport.service;

import com.fisport.common.EPaymentMethod;
import com.fisport.common.ETransactionStatus;
import com.fisport.common.ETransactionType;
import com.fisport.model.Transaction;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TransactionSpecification {

    private TransactionSpecification() {

    }

    public static Specification<Transaction> filterTransaction(BigDecimal amount,
                                                               EPaymentMethod paymentMethod,
                                                               ETransactionType transactionType,
                                                               ETransactionStatus transactionStatus,
                                                               LocalDateTime fromDate, LocalDateTime toDate,
                                                               Long walletId) {

        return (Root<Transaction> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            Join<?, ?> joinWallet = root.join("wallet");

            Predicate predicate = cb.conjunction();

            if (amount != null) {
                predicate = cb.and(predicate, cb.equal(cb.function("ROUND", BigDecimal.class, cb.literal(2)), amount));
            }

            if (paymentMethod != null) {
                predicate = cb.and(predicate, cb.equal(root.get("method"), paymentMethod));
            }

            if (transactionType != null) {
                predicate = cb.and(predicate, cb.equal(root.get("type"), transactionType));
            }

            if (transactionStatus != null) {
                predicate = cb.and(predicate, cb.equal(root.get("status"), transactionStatus));
            }

            if (fromDate != null) {
                predicate = cb.and(predicate, cb.greaterThanOrEqualTo(root.get("createdAt"), fromDate));
            }

            if (toDate != null) {
                predicate = cb.and(predicate, cb.lessThanOrEqualTo(root.get("createdAt"), toDate));
            }

            if (walletId != null) {
                predicate = cb.and(predicate, cb.equal(joinWallet.get("id"), walletId));
            }

            return predicate;
        };

    }
}
