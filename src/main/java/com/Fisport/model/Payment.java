package com.Fisport.model;

import com.Fisport.common.EPaymentMethod;
import com.Fisport.common.EPaymentStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "payment")
public class Payment extends AbstractEntity {

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EPaymentMethod method;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EPaymentStatus status;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(unique = true)
    private String transactionId;

    @Column(name = "payment_time")
    private LocalDateTime paymentTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id", nullable = false)
    private Booking booking;
}
