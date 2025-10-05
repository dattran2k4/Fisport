package com.Fisport.api;

import com.Fisport.dto.request.ChangePasswordRequest;
import com.Fisport.dto.request.UpdateProfileRequest;
import com.Fisport.dto.response.ResponseData;
import com.Fisport.dto.response.ResponseError;
import com.Fisport.dto.response.UserResponse;
import com.Fisport.service.UserService;
import com.Fisport.util.ERole;
import com.Fisport.util.EUserStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/users")
public class UserApiController {
    private final UserService userService;

    @GetMapping("/")
    public ResponseData<?> getProfile(Principal principal) {
        try {
            return new ResponseData<>(HttpStatus.OK.value(), "Get profile successfully", userService.getUserByUserName(principal.getName()));
        } catch (Exception e) {
            return new ResponseError(HttpStatus.NOT_FOUND.value(), "Get profile failed");
        }
    }

    @PutMapping("/update")
    public ResponseData<?> updateProfile(@RequestBody UpdateProfileRequest request, Principal principal) {
        try {
            userService.updateUserByUserName(request, principal.getName());
            return new ResponseData<>(HttpStatus.ACCEPTED.value(), "Update profile successfully");
        } catch (Exception e) {
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Update profile failed");
        }
    }

    @PatchMapping("/change-password")
    public ResponseData<?> changePassword(@RequestBody ChangePasswordRequest request, Principal principal) {
        try {
            userService.changePasswordByUserName(request, principal.getName());
            return new ResponseData<>(HttpStatus.ACCEPTED.value(), "Change password successfully");
        } catch (Exception e) {
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Change password failed");
        }
    }

    @GetMapping("/list")
    public ResponseData<?> getAllUsers(@RequestParam(required = false) String keyword) {
        try {
            List<UserResponse> users = userService.getAllUsers(keyword);
            return new ResponseData<>(HttpStatus.OK.value(), "Get all users successfully", users);
        } catch (Exception e) {
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Get all users failed");
        }
    }

    @PatchMapping("/{id}/change-status")
    public ResponseData<?> changeStatus(@PathVariable Long id, @RequestParam EUserStatus status) {
        try {
            userService.changeStatus(id, status);
            return new ResponseData<>(HttpStatus.ACCEPTED.value(), "Change status successfully");
        } catch(Exception e) {
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Change status failed");
        }
    }

    @PatchMapping("/{id}/assign-role")
    public ResponseData<?> assignRole(@PathVariable Long id, @RequestParam ERole role) {
        try {
            userService.assignRole(id, role);
            return new ResponseData<>(HttpStatus.ACCEPTED.value(), "Assign role successfully");
        } catch (Exception e) {
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Assign role failed");
        }
    }
}
