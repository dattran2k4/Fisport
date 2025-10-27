package com.fisport.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "challenge_type")
public class ChallengeType extends AbstractEntity {

    @Column(name = "type")
    private String name;

    @Column(name = "max_players_per_team")
    private Integer maxPlayersPerTeam;

    @Column(name = "total_teams")
    private Integer totalTeams;

    @ManyToOne
    @JoinColumn(name = "sport_id")
    private FieldType sport;
}
