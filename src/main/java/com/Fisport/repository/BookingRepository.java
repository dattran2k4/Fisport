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

import java.math.BigDecimal;
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
            "AND b.bookingStatus IN ('PENDING','CONFIRMED') " +
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

        @Query("SELECT COUNT(b) FROM Booking b WHERE b.bookingStatus = com.Fisport.common.EBookingStatus.PENDING")
        long countByPendingStatus();

        @Query("SELECT FUNCTION('DATE', b.bookingDate) as d, COUNT(b) FROM Booking b WHERE b.bookingDate >= :from AND b.bookingDate <= :to GROUP BY b.bookingDate ORDER BY b.bookingDate")
        java.util.List<java.lang.Object[]> countByDateRange(@Param("from") LocalDate from, @Param("to") LocalDate to);
    // Count bookings with date filter
    @Query("SELECT COUNT(b) FROM Booking b " +
            "WHERE (:ownerId IS NULL OR b.subfield.field.owner.id = :ownerId) " +
            "AND b.bookingDate BETWEEN :from AND :to")
    Long countBookings(@Param("ownerId") Long ownerId,
                       @Param("from") LocalDate from,
                       @Param("to") LocalDate to);

    // Sum revenue
    @Query("SELECT COALESCE(SUM(b.totalPrice), 0) FROM Booking b " +
            "WHERE (:ownerId IS NULL OR b.subfield.field.owner.id = :ownerId) " +
            "AND b.bookingDate BETWEEN :from AND :to " +
            "AND (:status IS NULL OR b.bookingStatus = :status)")
    BigDecimal sumRevenue(@Param("ownerId") Long ownerId,
                          @Param("from") LocalDate from,
                          @Param("to") LocalDate to,
                          @Param("status") EBookingStatus status);

    // Revenue grouped by date
    @Query("SELECT b.bookingDate, SUM(b.totalPrice) FROM Booking b " +
            "WHERE (:ownerId IS NULL OR b.subfield.field.owner.id = :ownerId) " +
            "AND b.bookingDate BETWEEN :from AND :to " +
            "GROUP BY b.bookingDate ORDER BY b.bookingDate")
    List<Object[]> revenueGroupByDate(@Param("ownerId") Long ownerId,
                                      @Param("from") LocalDate from,
                                      @Param("to") LocalDate to);

    // Top fields by revenue
    @Query("SELECT f.id, f.name, COUNT(b), SUM(b.totalPrice) FROM Booking b " +
            "JOIN b.subfield sf JOIN sf.field f " +
            "WHERE (:ownerId IS NULL OR f.owner.id = :ownerId) " +
            "GROUP BY f.id, f.name " +
            "ORDER BY SUM(b.totalPrice) DESC")
    List<Object[]> topFieldsByRevenue(@Param("ownerId") Long ownerId,
                                      @Param("limit") int limit);

    // Count bookings by status (using Enum)
    @Query("SELECT COUNT(b) FROM Booking b " +
            "WHERE (:ownerId IS NULL OR b.subfield.field.owner.id = :ownerId) " +
            "AND b.bookingDate BETWEEN :from AND :to " +
            "AND b.bookingStatus = :status")
    Long countByStatus(@Param("ownerId") Long ownerId,
                       @Param("from") LocalDate from,
                       @Param("to") LocalDate to,
                       @Param("status") EBookingStatus status);

    // Bookings grouped by status
    @Query("SELECT CAST(b.bookingStatus AS string), COUNT(b) FROM Booking b " +
            "WHERE (:ownerId IS NULL OR b.subfield.field.owner.id = :ownerId) " +
            "AND b.bookingDate BETWEEN :from AND :to " +
            "GROUP BY b.bookingStatus")
    List<Object[]> countByStatusGroup(@Param("ownerId") Long ownerId,
                                      @Param("from") LocalDate from,
                                      @Param("to") LocalDate to);

    // Bookings by hour
    @Query("SELECT HOUR(b.startTime), COUNT(b) FROM Booking b " +
            "WHERE (:ownerId IS NULL OR b.subfield.field.owner.id = :ownerId) " +
            "AND b.bookingDate BETWEEN :from AND :to " +
            "GROUP BY HOUR(b.startTime) ORDER BY HOUR(b.startTime)")
    List<Object[]> bookingsByHour(@Param("ownerId") Long ownerId,
                                  @Param("from") LocalDate from,
                                  @Param("to") LocalDate to);
}
