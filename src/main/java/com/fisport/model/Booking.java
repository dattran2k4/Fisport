package com.fisport.model;

import com.fisport.common.EBookingStatus;
import com.fisport.common.EPaymentMethod;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

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

    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;

    @Column(name = "duration", nullable = false)
    private Integer duration;

    @Column(name = "total_price", nullable = false)
    private BigDecimal totalPrice;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 50)
    private EBookingStatus bookingStatus;

    @ManyToOne
    @JoinColumn(name = "subfield_id")
    private SubField subfield;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne(mappedBy = "booking")
    private Review review;

    @Builder.Default
    @OneToMany(mappedBy = "booking", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<BookingServiceItem> bookingServiceItems = new HashSet<>();

    @OneToOne(mappedBy = "booking", fetch = FetchType.EAGER)
    private ChallengeMatch challengeMatch;

    @OneToMany(mappedBy = "booking")
    private List<Transaction> transaction = new ArrayList<>();

    @OneToOne(mappedBy = "booking")
    private Payment payment;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method")
    private EPaymentMethod  paymentMethod;

    @Column(name = "payment_token")
    private String paymentToken;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "expired_at")
    private LocalDateTime expiredAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        paymentToken = UUID.randomUUID().toString();
    }
}
