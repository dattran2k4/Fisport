package com.fisport.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "field_time_slot")
public class FieldHasTimeSlot extends AbstractEntity {

    @ManyToOne
    @JoinColumn(name = "field_id",  nullable = false)
    private Field field;

    @ManyToOne
    @JoinColumn(name = "time_slot_id", nullable = false)
    private TimeSlot timeSlot;

    @Column(name = "price", nullable = false)
    private BigDecimal price;

}
