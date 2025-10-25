package com.fisport.dto.response;

import com.fisport.common.EGender;
import com.fisport.common.EUserStatus;
import com.fisport.model.Voucher;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Builder
@Getter
public class UserResponse {
    private Long id;
    private String username;
    private String email;
    private String phone;
    private LocalDate birthday;
    private EGender gender;
    private EUserStatus status;
    private String roleName;
    private Set<Voucher> vouchers;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
