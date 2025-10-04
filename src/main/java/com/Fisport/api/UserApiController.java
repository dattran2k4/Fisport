package com.Fisport.api;

import com.Fisport.dto.request.ChangePasswordRequest;
import com.Fisport.dto.request.UpdateProfileRequest;
import com.Fisport.dto.response.ResponseData;
import com.Fisport.dto.response.ResponseError;
import com.Fisport.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
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

    @PutMapping()
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
}
