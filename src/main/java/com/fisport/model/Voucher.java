package com.fisport.model;

import com.fisport.common.EVoucherStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.*;
import java.util.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "voucher")
public class Voucher extends AbstractEntity {

    @Column(name = "code", unique = true)
    private String code;

    @Column(name = "description")
    private String description;

    private Integer discount;

    @Enumerated(EnumType.STRING)
    private EVoucherStatus status;

    @Column(name = "limit_qnt")
    private Integer limit;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToMany(mappedBy = "vouchers")
    @JsonIgnore
    private Set<User> users = new HashSet<>();
}
