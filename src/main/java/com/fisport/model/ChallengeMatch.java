package com.fisport.model;

import com.fisport.common.EChallengeStatus;
import com.fisport.common.ELevel;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "challenge_match")
public class ChallengeMatch extends AbstractEntity {

    @ManyToOne
    @JoinColumn(name = "creator_id", nullable = false)
    private User creator;

    @ManyToOne
    @JoinColumn(name = "sport_id")
    private FieldType fieldType;

    @Column(name = "title", length = 100)
    private String title;

    @Enumerated(EnumType.STRING)
    private ELevel suggestedLevel;

    @Column(name = "fee")
    @Min(1)
    private BigDecimal participationFee = BigDecimal.ZERO;

    @Column(name = "max_players")
    private int maxPlayers;

    @ManyToOne
    @JoinColumn(name = "subfield_id")
    private SubField subField;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "start_time")
    private LocalTime startTime;

    @Column(name = "end_time")
    private LocalTime endTime;

    @Column(name = "note", length = 500)
    private String note;

    @Enumerated(EnumType.STRING)
    private EChallengeStatus status;

    @OneToOne
    @JoinColumn(name = "booking_id", nullable = false)
    private Booking booking;

    @OneToMany(mappedBy = "match")
    private List<ChallengeParticipant> participants = new ArrayList<>();

    @OneToMany(mappedBy = "match")
    private List<ChallengeResult> results;

    @Column(name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @CreationTimestamp
    private LocalDateTime updatedAt;

}
