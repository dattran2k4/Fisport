package com.Fisport.api;

import com.Fisport.dto.request.TwoFARequest;
import com.Fisport.dto.response.LoginResponse;
import com.Fisport.dto.response.ResponseData;
import com.Fisport.dto.response.ResponseError;
import com.Fisport.model.User;
import com.Fisport.service.AuthService;
import com.Fisport.service.TwoFAService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth/2fa")
public class TwoFactorAuthApiController {

    private final TwoFAService twoFAService;
    private final AuthService authService;

    @PostMapping("/enable")
    public ResponseData<?> enable2Fa(@AuthenticationPrincipal User user, @RequestParam int code ) {
        twoFAService.enable2Fa(user, code);
        return ResponseData.builder()
                .status(HttpStatus.ACCEPTED.value())
                .message("2FA đã được bật thành công")
                .build();
    }

    @PostMapping("/disable")
    public ResponseData<?> disable2Fa(@AuthenticationPrincipal User user, @RequestParam int code ) {
        twoFAService.disable2Fa(user, code);
        return ResponseData.builder()
                .status(HttpStatus.ACCEPTED.value())
                .message("2FA đã được tắt thành công")
                .build();
    }

    @PostMapping("/verify")
    public ResponseData<?> verify2FA(@RequestBody TwoFARequest twoFARequest, HttpSession session) {
        try {
            LoginResponse response = authService.verify2FA(twoFARequest, session);
            return new ResponseData(HttpStatus.ACCEPTED.value(),"login success" , response);
        } catch (Exception e) {
            return new ResponseError(HttpStatus.BAD_REQUEST.value(),e.getMessage());
        }
    }
}
