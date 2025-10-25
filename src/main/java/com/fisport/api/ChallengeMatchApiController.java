package com.fisport.api;

import com.fisport.dto.request.ChallengeMatchRequest;
import com.fisport.dto.response.ApiResponse;
import com.fisport.service.ChallengeMatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RequiredArgsConstructor
@RestController
@Validated
@RequestMapping("/api/v1/challenge-matchs")
public class ChallengeMatchApiController {

    private final ChallengeMatchService challengeMatchService;

    @PostMapping("/create")
    public ApiResponse createChallengeMatch(@RequestBody ChallengeMatchRequest request, Principal principal) {
        String paymentToken = challengeMatchService.createChallengeMatch(request,principal.getName());
        return ApiResponse.builder()
                .status(HttpStatus.CREATED.value())
                .data(paymentToken)
                .message("Đã tạo thách đấu, chuyển hướng đến thanh toán")
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse getChallengeMatchById(@PathVariable Long id) {

        return ApiResponse.builder().build();
    }

}
