package com.Fisport.service;

import com.Fisport.dto.request.ChangePasswordRequest;
import com.Fisport.dto.request.UpdateProfileRequest;
import com.Fisport.dto.response.UserResponse;
import com.Fisport.common.ERole;
import com.Fisport.common.EUserStatus;

import java.util.List;

public interface UserService {
    UserResponse getUserById(Long id);
    UserResponse getUserByUserName(String name);
    void updateUserByUserName(UpdateProfileRequest request, String name);
    void changePasswordByUserName(ChangePasswordRequest request, String name);
    List<UserResponse> getAllUsers(String keyword);
    org.springframework.data.domain.Page<com.Fisport.dto.response.UserResponse> getAllUsersPaged(String keyword, com.Fisport.common.ERole role, com.Fisport.common.EUserStatus status, int page, int size);

    void changeStatus(Long id, EUserStatus status);

    void assignRole(Long id, ERole role);
    void deleteUser(Long id);
    void adminUpdateUser(Long id, com.Fisport.dto.request.UpdateProfileRequest request, ERole role, EUserStatus status);
    void createUser(String username, String rawPassword, com.Fisport.dto.request.UpdateProfileRequest request, ERole role, EUserStatus status);
}
