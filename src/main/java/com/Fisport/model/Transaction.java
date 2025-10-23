package com.Fisport.model;

import com.Fisport.common.EPaymentMethod;
import com.Fisport.common.ETransactionStatus;
import com.Fisport.common.ETransactionType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "transaction")
public class Transaction extends AbstractEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wallet_id", nullable = false)
    private Wallet wallet;

    @Column(nullable = false, precision = 18, scale = 2)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ETransactionType type; // TOPUP, PAYMENT, REFUND

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ETransactionStatus status;// PENDING, SUCCESS, FAILED

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id")
    private Booking booking;

    @OneToOne(mappedBy = "transaction")
    private Payment payment;

    @Column
    private EPaymentMethod method;

    @Column(nullable = false, updatable = false, name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;
}
