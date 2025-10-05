package com.Fisport.dto.response;

import com.Fisport.common.EGender;
import com.Fisport.common.EUserStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class LoginResponseDTO {
    private Long id;
    private String username;
    private String email;
    private String phone;
    private LocalDate birthday;
    private EGender gender;
    private EUserStatus status;
    private String roleName;
}
