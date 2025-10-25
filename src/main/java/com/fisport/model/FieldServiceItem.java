package com.fisport.model;

import com.fisport.common.EFieldServiceItem;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "field_service_item")
public class FieldServiceItem extends AbstractEntity {

    @Column(name = "price", nullable = false)
    private BigDecimal price;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private EFieldServiceItem status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "field_id")
    private Field field;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_item_id")
    private ServiceItem serviceItem;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "fieldServiceItem", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<BookingServiceItem> bookingServiceItems;
}
