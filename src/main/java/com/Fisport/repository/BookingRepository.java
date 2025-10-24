package com.Fisport.repository;

import com.Fisport.common.EBookingStatus;
import com.Fisport.model.Booking;
import com.Fisport.model.SubField;
import com.Fisport.model.User;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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

    List<Booking> findByUserId(Long userId);

    Optional<Booking> findByIdAndUser(Long id, User user);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT b FROM Booking b WHERE b.subfield.id = :subfieldId AND b.bookingDate = :date " +
            "AND b.bookingStatus IN ('PENDING','PAID') " +
            "AND ((:startTime BETWEEN b.startTime AND b.endTime) " +
            "OR (:endTime BETWEEN b.startTime AND b.endTime) " +
            "OR (b.startTime BETWEEN :startTime AND :endTime))")
    List<Booking> findOverlappingBookingsForUpdate(@Param("subfieldId") Long subfieldId,
                                                   @Param("date") LocalDate date,
                                                   @Param("startTime") LocalTime startTime,
                                                   @Param("endTime") LocalTime endTime);


    @Modifying
    @Transactional
    @Query("UPDATE Booking b SET b.bookingStatus = :pending " +
            "WHERE b.bookingStatus = :fail " +
            "AND b.expiredAt < :now")
    int updateExpriedBooking(@Param("pending") EBookingStatus pending,
                             @Param("fail") EBookingStatus fail,
                             @Param("now") LocalDateTime now);

    List<Booking> findBySubfieldAndBookingDate(SubField subField, LocalDate date);
}
