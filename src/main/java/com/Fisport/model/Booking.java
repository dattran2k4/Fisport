package com.Fisport.model;

import com.Fisport.common.EBookingStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "booking")
public class Booking extends AbstractEntity {

    @Column(name = "booking_date")
    private LocalDate bookingDate;

    @Column(name = "end_time",  nullable = false)
    private LocalTime endTime;

    @Column(name = "duration",  nullable = false)
    private int duration;

    @Column(name = "total_price", nullable = false)
    private BigDecimal totalPrice;

    @Column(name = "status", nullable = false, length = 50)
    private EBookingStatus bookingStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "field_time_slot_id")
    private FieldHasTimeSlot fieldTimeSlot;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "booking")
    private Review review;

    @OneToMany(mappedBy = "booking",  cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<BookingServiceItem> bookingServiceItems = new HashSet<BookingServiceItem>();

    @CreationTimestamp
    @Column(name = "create_at", updatable = false)
    private LocalDateTime createdAt;
}
