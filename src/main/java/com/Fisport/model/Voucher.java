package com.Fisport.model;

import com.Fisport.common.EVoucherStatus;
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
@Table(name = "voucher")
public class Voucher extends AbstractEntity {

    private String code;

    private String description;

    private Integer discountPercent;

    @Enumerated(EnumType.STRING)
    private EVoucherStatus status;

    @ManyToMany(mappedBy = "vouchers")
    private Set<User> users = new HashSet<>();


}
