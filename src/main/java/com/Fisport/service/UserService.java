package com.Fisport.service;

import com.Fisport.dto.request.ChangePasswordRequest;
import com.Fisport.dto.request.UpdateProfileRequest;
import com.Fisport.dto.response.UserResponse;

public interface UserService {
    UserResponse getUserById(Long id);

    UserResponse getUserByUserName(String name);

    void updateUserByUserName(UpdateProfileRequest request, String name);

    void changePasswordByUserName(ChangePasswordRequest request, String name);
}
