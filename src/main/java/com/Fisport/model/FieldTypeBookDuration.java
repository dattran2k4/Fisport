package com.Fisport.model;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="field_type_id")
    private FieldType fieldType;

    @ManyToOne(fetch = FetchType.LAZY,  cascade = CascadeType.ALL,  optional = false)
    @JoinColumn(name = "duration_id")
    private Duration duration;
}
