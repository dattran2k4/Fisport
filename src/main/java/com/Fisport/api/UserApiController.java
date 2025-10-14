package com.Fisport.api;

import com.Fisport.dto.request.ChangePasswordRequest;
import com.Fisport.dto.request.UpdateProfileRequest;
import com.Fisport.dto.response.*;
import com.Fisport.security.CustomUserDetails;
import com.Fisport.service.UserService;
import com.Fisport.common.ERole;
import com.Fisport.common.EUserStatus;
import com.Fisport.service.VoucherService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/users")
public class UserApiController {
    private final UserService userService;
    private final VoucherService voucherService;

    @GetMapping("/")
    public ApiResponse<?> getProfile(Principal principal) {
        return ApiResponse.builder()
                .status(HttpStatus.FOUND.value())
                .message("Get profile successfully")
                .data(userService.getUserByUserName(principal.getName()))
                .build();
    }

    @PutMapping("/update")
    public ApiResponse<?> updateProfile(@RequestBody UpdateProfileRequest request, Principal principal) {
        userService.updateUserByUserName(request, principal.getName());
        return ApiResponse.builder()
                .status(HttpStatus.ACCEPTED.value())
                .message("Update profile successfully")
                .build();
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
        } catch (Exception e) {
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

    @GetMapping("/vouchers")
    public ApiResponse<?> getVouchersForBooking(@AuthenticationPrincipal CustomUserDetails principal) {
        List<VoucherResponse> response = voucherService.getVouchersByUserId(principal.getUser().getId());

        return ApiResponse.builder()
                .status(HttpStatus.FOUND.value())
                .data(response)
                .message("get vouchers successfully")
                .build();

    }
}
