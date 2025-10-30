package com.fisport.model;

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
@Table(name = "service")
public class Service extends AbstractEntity {

    @Column(name = "name",  nullable = false)
    private String name;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "service",  orphanRemoval = true)
    private Set<ServiceItem> serviceItems = new HashSet<ServiceItem>();
}
