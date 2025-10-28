package com.fisport.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "field_type")
public class FieldType extends AbstractEntity {
    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false, unique = true)
    private String slug;

    @OneToMany(mappedBy = "fieldType", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Field> fields = new LinkedHashSet<>();

    @OneToMany(mappedBy = "fieldType")
    private Set<FieldTypeBookDuration> fieldTypeBookDuration = new LinkedHashSet<>();

    @OneToMany(mappedBy = "sport")
    private Set<UserSportElo> sportElos = new HashSet<>();

    @OneToMany(mappedBy = "sport")
    private List<ChallengeMatchType> challengeMatchTypes = new ArrayList<>();
}
