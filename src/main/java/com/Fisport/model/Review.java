package com.Fisport.model;

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
@Table(name = "review")
public class Review extends AbstractEntity {

    @Column(name = "rating", nullable = false)
    private Integer rating;

    @Column(name = "comment", length = 255)
    private String comment;

    @ManyToOne()
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne()
    @JoinColumn(name = "field_id")
    private Field field;

    @OneToOne()
    @JoinColumn(name = "booking_id", nullable = true)
    private Booking booking;

    @CreationTimestamp
    @Column(name = "create_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
