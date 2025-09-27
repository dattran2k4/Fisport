package com.Fisport.model;

import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "duration")
public class Duration extends AbstractEntity {

    @Column(name ="minutes", nullable = false)
    private int minutes;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL,  orphanRemoval = true, mappedBy = "duration")
    private Set<FieldTypeBookDuration> fieldTypeBookDuration;
}
