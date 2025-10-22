package com.Fisport.repository;

import com.Fisport.model.Payment;
import com.Fisport.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    Transaction findByPayment(Payment payment);
}
