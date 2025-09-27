package com.Fisport.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "service_item")
public class ServiceItem extends AbstractEntity {

    private String name;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "brand_id")
    private Brand brand;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "service_id")
    private Service  service;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "serviceItem",   cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<FieldServiceItem> fieldServiceItems = new HashSet<FieldServiceItem>();
}
