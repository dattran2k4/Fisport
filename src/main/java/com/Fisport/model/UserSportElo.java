package com.Fisport.model;

import com.Fisport.common.ELevel;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_sport_elo")
public class UserSportElo extends AbstractEntity {

    @ManyToOne
    @JoinColumn(name = "user_id",  nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "field_type_id")
    private FieldType sport;

    @Column(name = "elo")
    private int elo;

    @Enumerated(EnumType.STRING)
    @Column(name = "level")
    private ELevel level;

    @Column(name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
