package com.fisport.model;

import com.fisport.common.EParticipantStatus;
import com.fisport.common.ETeam;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

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

    @Column(name = "request_message", length = 200)
    private String requestMessage;

    @Column(name = "response_message", length = 200)
    private String responseMessage;

    @Enumerated(EnumType.STRING)
    private ETeam team;

    @Column(name = "paid")
    private boolean paid;

    @Column(name = "paid_at")
    private LocalDateTime paidAt;

    @Column(name = "cancelled_at")
    private LocalDateTime cancelledAt;

    @Column(name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
