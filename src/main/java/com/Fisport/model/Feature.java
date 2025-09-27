package com.Fisport.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "feature")
public class Feature extends AbstractEntity {

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "slug", nullable = false, unique = true)
    private String slug;

    @OneToMany(mappedBy = "feature", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<FieldHasFeature> fieldHasFeatures =  new HashSet<>();
}
