package com.Fisport.model;

import com.Fisport.common.EChallengeStatus;
import com.Fisport.common.ELevel;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
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

    @Column(name = "end_time", length = 100)
    private String title;

    @Enumerated(EnumType.STRING)
    private ELevel suggestedLevel;

    @Column(name = "fee")
    private BigDecimal participationFee = BigDecimal.ZERO;

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

    @OneToMany(mappedBy = "match")
    private List<ChallengeParticipant> participants = new ArrayList<>();

    @OneToMany(mappedBy = "match")
    private List<ChallengeResult> results;

}
