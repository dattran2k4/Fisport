package com.Fisport.service.impl;

import com.Fisport.dto.request.ChangePasswordRequest;
import com.Fisport.dto.request.UpdateProfileRequest;
import com.Fisport.dto.response.UserResponse;
import com.Fisport.exception.ResourceNotFoundException;
import com.Fisport.model.User;
import com.Fisport.repository.UserRepository;
import com.Fisport.service.UserService;
import com.Fisport.util.ERole;
import com.Fisport.util.EUserStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class IUserService implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResponse getUserByUserName(String name) {
        Optional<User> user = userRepository.findByUsername(name);
        return UserResponse.builder()
                .id(user.get().getId())
                .username(user.get().getUsername())
                .email(user.get().getEmail())
                .phone(user.get().getPhone())
                .birthday(user.get().getBirthday())
                .gender(user.get().getGender())
                .roleName(String.valueOf(user.get().getRole().getName()))
                .status(user.get().getStatus())
                .createdAt(user.get().getCreatedAt())
                .updatedAt(user.get().getUpdatedAt())
                .build();
    }

    @Override
    public void updateUserByUserName(UpdateProfileRequest request, String name) {
        User user = userRepository.findByUsername(name).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy user"));
        user.setPhone(request.getPhone());
        user.setEmail(request.getEmail());
        user.setGender(request.getGender());
        user.setBirthday(request.getBirthday());

        userRepository.save(user);
    }

    @Override
    public void changePasswordByUserName(ChangePasswordRequest request, String name) {
        User user = userRepository.findByUsername(name).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy user"));

        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new RuntimeException("Mật khẩu hiện tại không đúng");
        }

        if (!request.getNewPassword().equals(request.getConfirmNewPassword())) {
            throw new RuntimeException("Mật khẩu mới và xác nhận mật khẩu mới không khớp");
        }

        if (passwordEncoder.matches(request.getNewPassword(), user.getPassword())) {
            throw new RuntimeException("Mật khẩu mới trùng với mật khẩu cũ");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }

    @Override
    public List<UserResponse> getAllUsers(String keyword) {
        List<User> users = userRepository.searchByKeyword(keyword);
        return users.stream().map(this::toUserResponse).toList();
    }

    @Override
    public void changeStatus(Long id, EUserStatus status) {
        User user = getUser(id);
        user.setStatus(status);
        userRepository.save(user);
    }

    @Override
    public void assignRole(Long id, ERole role) {
        User user = getUser(id);
        user.getRole().setName(role);
        userRepository.save(user);
    }


    @Override
    public UserResponse getUserById(Long id) {
        User user = getUser(id);
        return toUserResponse(user);
    }

    public User getUser(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy user"));
    }

    private UserResponse toUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .phone(user.getPhone())
                .birthday(user.getBirthday())
                .gender(user.getGender())
                .roleName(String.valueOf(user.getRole().getName()))
                .status(user.getStatus())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }

}
