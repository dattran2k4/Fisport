package com.Fisport.api;

import com.Fisport.dto.response.ResponseData;
import com.Fisport.model.User;
import com.Fisport.service.TwoFAService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth/2fa")
public class TwoFactorAuthApiController {

    private final TwoFAService twoFAService;

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
}
