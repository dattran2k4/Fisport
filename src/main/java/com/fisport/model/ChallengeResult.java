package com.fisport.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "challenge_result")
public class ChallengeResult extends AbstractEntity {

    @ManyToOne
    @JoinColumn(name = "match_id", nullable = false)
    private ChallengeMatch match;

    @ManyToOne
    @JoinColumn(name = "player_id", nullable = false)
    private User player;

    @Min(0)
    private Integer score;

    @Column(name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;
}
