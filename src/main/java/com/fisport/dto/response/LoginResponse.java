package com.fisport.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Builder
@Getter
public class LoginResponse {
    private Long userId;
    private String username;
    private String email;
    private String phoneNumber;
    private String gender;
    private LocalDate birthDate;
    private boolean is2FAEnabled;
    private String message;
    private String role;
}
