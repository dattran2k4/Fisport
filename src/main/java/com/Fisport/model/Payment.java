package com.Fisport.model;

import com.Fisport.common.EPaymentMethod;
import com.Fisport.common.EPaymentStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

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

    @Column(name = "transaction_code", unique = true)
    private String transactionCode;

    @Column(name = "payment_time")
    private LocalDateTime paymentTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id")
    private Booking booking;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transaction_id")
    private Transaction transaction;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createAt;
}
