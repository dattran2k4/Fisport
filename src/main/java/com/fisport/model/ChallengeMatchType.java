package com.fisport.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "challenge_match_type")
public class ChallengeMatchType extends AbstractEntity {

    @Column(name = "name")
    private String name;

    @Column(name = "max_players")
    @Min(1)
    private Integer maxPlayers;

    @ManyToOne
    @JoinColumn(name = "field_type_id")
    private FieldType fieldType;

    @OneToMany(mappedBy = "challengeMatchType")
    private Set<ChallengeMatch> challengeMatch = new HashSet<>();
}
