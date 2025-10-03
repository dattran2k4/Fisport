package com.Fisport.dto.response;

import com.Fisport.util.EGender;
import com.Fisport.util.EUserStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

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
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
