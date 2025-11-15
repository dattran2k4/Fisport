package com.fisport.api;

import com.fisport.dto.response.ApiResponse;
import com.fisport.security.CustomUserDetails;
import com.fisport.service.VoucherService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/vouchers")
@Slf4j(topic = "VOUCHER-API-CONTROLLER")
public class VoucherApiController {

    private final VoucherService voucherService;

    @GetMapping("/list")
    public ApiResponse getAllVouchersByUser(@AuthenticationPrincipal CustomUserDetails principal) {
        return ApiResponse.builder()
                .status(200)
                .message("OK")
                .data(voucherService.getVouchersByUserId(principal.getUser().getId()))
                .build();
    }

}
