package com.fisport.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "field_type_duration")
public class FieldTypeBookDuration extends AbstractEntity {

    @ManyToOne
    @JoinColumn(name ="field_type_id")
    private FieldType fieldType;

    @ManyToOne()
    @JoinColumn(name = "duration_id")
    private Duration duration;
}
