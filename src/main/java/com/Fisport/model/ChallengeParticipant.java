package com.Fisport.model;

import com.Fisport.common.EParticipantStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "challenge_participant")
public class ChallengeParticipant extends AbstractEntity {

    @ManyToOne
    @JoinColumn(name = "match_id", nullable = false)
    private ChallengeMatch match;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    private EParticipantStatus status;

    @Column(name = "request_message")
    private String requestMessage;

    @Column(name = "response_message")
    private String responseMessage;

    @Column(name = "paid")
    private boolean paid;

    @Column(name = "paid_at")
    private LocalDateTime paidAt;

    @Column(name = "cancelled_at")
    private LocalDateTime cancelledAt;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "created_at")
    private LocalDateTime updatedAt;
}
