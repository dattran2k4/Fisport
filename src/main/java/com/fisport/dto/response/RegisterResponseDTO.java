package com.fisport.dto.response;

import com.fisport.common.EGender;
import com.fisport.common.EUserStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Builder
@Getter
public class RegisterResponseDTO {
    private Long id;
    private String username;
    private String email;
    private String phone;
    private LocalDate birthday;
    private EGender gender;
    private EUserStatus status;
    private String roleName;
}