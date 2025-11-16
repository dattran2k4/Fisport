package com.fisport.model;

import com.fisport.common.EGender;
import com.fisport.common.EUserStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user")
public class User extends AbstractEntity {

    @Column(unique = true, nullable = false, length = 30)
    private String username;

    @Column(length = 255)
    private String password;

    @Column(unique = true, nullable = false, length = 255)
    private String email;

    @Column(unique = true, length = 20)
    private String phone;

    @Column(name = "birth_day")
    private LocalDate birthday;

    @Enumerated(EnumType.STRING)
    private EGender gender;

    @Enumerated(EnumType.STRING)
    private EUserStatus status;

    @Column(name = "is_two_factor")
    private boolean twoFAEnable = false;

    @Column(name = "two_fa_secret")
    private String twoFASecret;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id")
    private Role role;

    @OneToMany(mappedBy = "owner")
    @Builder.Default
    private Set<Field> fields = new HashSet<>();

    @OneToMany(mappedBy = "user")
    @Builder.Default
    private Set<Booking> bookings = new HashSet<>();

    @OneToMany(mappedBy = "user")
    @Builder.Default
    private Set<Review> reviews = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "user_voucher",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "voucher_id")
    )
    private Set<Voucher> vouchers = new HashSet<>();

    @OneToMany(mappedBy = "user")
    private Set<UserSportElo> sportElos = new HashSet<>();

    @OneToMany(mappedBy = "user")
    private List<ChallengeParticipant> challengeParticipant = new ArrayList<>();

    @OneToOne(mappedBy = "user")
    private Wallet wallet;

    @CreationTimestamp
    @Column(name = "create_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "update_at")
    private LocalDateTime updatedAt;
}
