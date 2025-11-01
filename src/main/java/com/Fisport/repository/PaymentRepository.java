package com.Fisport.repository;

import com.Fisport.model.Booking;
import com.Fisport.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Payment findByBooking(Booking booking);

    @Query("SELECT COALESCE(SUM(p.amount), 0) FROM Payment p WHERE p.createAt >= :from AND p.createAt <= :to")
    BigDecimal sumAmountBetween(@Param("from") LocalDateTime from, @Param("to") LocalDateTime to);

    java.util.List<Payment> findTop5ByOrderByCreateAtDesc();
}
