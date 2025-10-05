package com.Fisport.model;

import com.Fisport.common.ESubFieldStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

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

    @ManyToOne
    @JoinColumn(name = "field_id")
    private Field field;

    @CreationTimestamp
    @Column(name = "create_at", updatable = false)
    private LocalDateTime create_at;

    @UpdateTimestamp
    @Column(name = "update_at")
    private LocalDateTime update_at;
}
