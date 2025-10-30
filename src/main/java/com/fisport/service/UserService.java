package com.fisport.service;

import com.fisport.dto.request.ChangePasswordRequest;
import com.fisport.dto.request.UpdateProfileRequest;
import com.fisport.dto.response.UserResponse;
import com.fisport.common.ERole;
import com.fisport.common.EUserStatus;

import java.util.List;

public interface UserService {
    UserResponse getUserById(Long id);
    UserResponse getUserByUserName(String name);
    void updateUserByUserName(UpdateProfileRequest request, String name);
    void changePasswordByUserName(ChangePasswordRequest request, String name);
    List<UserResponse> getAllUsers(String keyword);

    void changeStatus(Long id, EUserStatus status);

    void assignRole(Long id, ERole role);
}
