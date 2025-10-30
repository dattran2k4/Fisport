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
@Table(name = "booking_service_item")
public class BookingServiceItem extends AbstractEntity {

    @Column(name = "quantity",  nullable = false)
    private Integer quantity;

    @Column(name = "subtotal")
    private BigDecimal subTotal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id")
    private Booking booking;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "field_service_item_id")
    private FieldServiceItem fieldServiceItem;


}
