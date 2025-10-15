package com.Fisport.repository;

import com.Fisport.common.EBookingStatus;
import com.Fisport.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query("SELECT b FROM Booking b WHERE b.subfield.id = :id " +
            "AND b.bookingDate = :date " +
            "AND b.bookingStatus IN :status")
    List<Booking> findOccupiedSlots(@Param("id") Long subFieldId, @Param("date") LocalDate date, @Param("status") List<EBookingStatus> bookingStatuses);

    @Query("SELECT b FROM Booking b WHERE b.paymentToken = :token " +
            "AND b.bookingStatus IN :status")
    Optional<Booking> findByPaymentTokenAndBookingStatus(@Param("token") String paymentToken, @Param("status") List<EBookingStatus> bookingStatuses);
}
