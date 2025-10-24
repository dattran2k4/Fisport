package com.Fisport.model;

import jakarta.persistence.*;
import lombok.*;

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

    private Integer score;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
