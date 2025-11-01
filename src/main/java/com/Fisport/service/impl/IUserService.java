package com.Fisport.service.impl;

import com.Fisport.dto.request.ChangePasswordRequest;
import com.Fisport.dto.request.UpdateProfileRequest;
import com.Fisport.dto.response.UserResponse;
import com.Fisport.exception.ResourceNotFoundException;
import com.Fisport.model.User;
import com.Fisport.repository.UserRepository;
import com.Fisport.service.UserService;
import com.Fisport.common.ERole;
import com.Fisport.common.EUserStatus;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@RequiredArgsConstructor
@Service
public class IUserService implements UserService {
    private final UserRepository userRepository;
    private final com.Fisport.repository.RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResponse getUserByUserName(String name) {
        User user = userRepository.findByUsername(name)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy user"));
        return toUserResponse(user);
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

    @Transactional
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
    public org.springframework.data.domain.Page<UserResponse> getAllUsersPaged(String keyword, com.Fisport.common.ERole role, com.Fisport.common.EUserStatus status, int page, int size) {
        Pageable pageable = PageRequest.of(Math.max(0, page), Math.max(1, size));
        Page<User> userPage = userRepository.findByFilters(keyword, role, status, pageable);

        List<UserResponse> content = userPage.stream().map(this::toUserResponse).toList();
        return new PageImpl<>(content, pageable, userPage.getTotalElements());
    }

    @Override
    public void changeStatus(Long id, EUserStatus status) {
        User user = getUser(id);
        user.setStatus(status);
        userRepository.save(user);
    }

    @Override
    public void deleteUser(Long id) {
        User user = getUser(id);
        userRepository.delete(user);
    }

    @Override
    public void adminUpdateUser(Long id, com.Fisport.dto.request.UpdateProfileRequest request, ERole role, EUserStatus status) {
        User user = getUser(id);
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setGender(request.getGender());
        user.setBirthday(request.getBirthday());
        user.setStatus(status);
        // set role by finding Role entity
        var roleEntity = roleRepository.findByName(role).orElseThrow(() -> new RuntimeException("Role not found"));
        user.setRole(roleEntity);
        userRepository.save(user);
    }

    @Override
    public void createUser(String username, String rawPassword, com.Fisport.dto.request.UpdateProfileRequest request, ERole role, EUserStatus status) {
        if (userRepository.existsByUsername(username)) {
            throw new RuntimeException("Username already exists");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }
        if (userRepository.existsByPhone(request.getPhone())) {
            throw new RuntimeException("Phone already exists");
        }

        var roleEntity = roleRepository.findByName(role).orElseThrow(() -> new RuntimeException("Role not found"));

        User user = User.builder()
                .username(username)
                .password(passwordEncoder.encode(rawPassword))
                .email(request.getEmail())
                .phone(request.getPhone())
                .birthday(request.getBirthday())
                .gender(request.getGender())
                .status(status)
                .role(roleEntity)
                .build();

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
