package com.Fisport.api;

import com.Fisport.dto.request.TwoFARequest;
import com.Fisport.dto.response.*;
import com.Fisport.model.User;
import com.Fisport.security.CustomUserDetails;
import com.Fisport.service.AuthService;
import com.Fisport.service.SecurityContextService;
import com.Fisport.service.TwoFAService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth/2fa")
public class TwoFactorAuthApiController {

    private final TwoFAService twoFAService;
    private final AuthService authService;

    @GetMapping("/generate-qr")
    public ApiResponse generateQR(Principal principal) {
        TwoFAResponse response = twoFAService.generate2FASecret(principal.getName());
        return ApiResponse.builder()
                .status(HttpStatus.CREATED.value())
                .message("QR code generated successfully")
                .data(response)
                .build();
    }

    @PostMapping("/enable")
    public ApiResponse<?> enable2Fa(Principal principal, @RequestBody Map<String, String> body) {
        twoFAService.enable2Fa(principal.getName(), body.get("code"));
        return ApiResponse.builder()
                .status(HttpStatus.ACCEPTED.value())
                .message("2FA đã được bật thành công")
                .build();
    }

    @PostMapping("/disable")
    public ApiResponse<?> disable2Fa(Principal principal, @RequestBody Map<String, String> body) {
        twoFAService.disable2Fa(principal.getName(), body.get("code"));
        return ApiResponse.builder()
                .status(HttpStatus.ACCEPTED.value())
                .message("2FA đã được tắt thành công")
                .build();
    }

    @PostMapping("/verify")
    public ApiResponse<?> verify2FA(@RequestBody TwoFARequest twoFARequest, HttpSession session) {
            boolean response = authService.verify2FA(twoFARequest);
            return ApiResponse.builder()
                    .status(HttpStatus.ACCEPTED.value())
                    .message("2fa")
                    .data(response)
                    .build();


    }
}
