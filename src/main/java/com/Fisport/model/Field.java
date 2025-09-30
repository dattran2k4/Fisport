package com.Fisport.model;

import com.Fisport.util.EFieldStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;


import java.time.LocalDateTime;
import java.util.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "field")
public class Field extends AbstractEntity {

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "banner", nullable = false)
    private String banner;

    @Column(name = "slug", nullable = false,  unique = true)
    private String slug;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private EFieldStatus fieldStatus;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User owner;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ward_id")
    private Ward ward;

    @OneToMany(mappedBy = "field",  fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<FieldHasTimeSlot> fieldTimeSlots = new HashSet<>();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "field_type_id")
    private FieldType fieldType;

    @OneToMany(mappedBy = "field")
    private Set<FieldHasFeature> fieldHasFeatures = new HashSet<>();

    @OneToMany(mappedBy = "field",  cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<FieldServiceItem> fieldServiceItems = new HashSet<>();

    @OneToMany(mappedBy = "field", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Review>  reviews = new HashSet<>();

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
