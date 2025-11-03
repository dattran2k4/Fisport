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

    @OneToOne
    @JoinColumn(name = "match_id", nullable = false)
    private ChallengeMatch match;

    @Column(name = "team_a_scort")
    @Min(0)
    private Integer teamAScort;

    @Column(name = "team_b_scort")
    @Min(0)
    private Integer teamBScort;

    @Column(name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;
}
