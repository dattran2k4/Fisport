package com.fisport.model;

import com.fisport.common.ESubFieldStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "sub_field")
public class SubField extends AbstractEntity {

    @Column(name = "name")
    private String name;

    @Enumerated(EnumType.STRING)
    private ESubFieldStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "field_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Field field;

    @OneToMany(mappedBy = "subfield")
    private Set<Booking> bookings = new HashSet<>();

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime create_at;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime update_at;
}
