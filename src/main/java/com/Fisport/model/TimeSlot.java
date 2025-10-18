package com.Fisport.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "time_slot")
public class TimeSlot extends AbstractEntity {

    @Column(name = "start_time",  nullable = false)
    private LocalTime startTime;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER,  orphanRemoval = true, mappedBy = "timeSlot")
    private List<FieldHasTimeSlot> fieldTimeSlots;
}
