package com.Fisport.repository;

import com.Fisport.model.Booking;
import com.Fisport.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Payment findByBooking(Booking booking);
}
