package com.fisport.api;

import com.fisport.common.EChallengeStatus;
import com.fisport.common.ELevel;
import com.fisport.dto.request.ChallengeMatchRequest;
import com.fisport.dto.response.ApiResponse;
import com.fisport.service.ChallengeMatchService;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.security.Principal;
import java.time.LocalDate;

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

    @GetMapping
    public ApiResponse getAllChallengeMatches(@RequestParam(required = false) EChallengeStatus status,
                                              @RequestParam(required = false) ELevel level,
                                              @Min(1)@RequestParam(required = false) Integer maxPlayers,
                                              @RequestParam(required = false) LocalDate date,
                                              @RequestParam(required = false) BigDecimal fee,
                                              @RequestParam(required = false) Long cityId,
                                              @RequestParam(required = false) Long fieldTypeId,
                                              @RequestParam(defaultValue = "0") int page,
                                              @RequestParam(defaultValue = "10") int size) {

        return ApiResponse.builder()
                .status(HttpStatus.FOUND.value())
                .message("challenge matches")
                .data(challengeMatchService.getAllChallengeMatch(status, level, maxPlayers, date, fee, cityId, fieldTypeId, page, size))
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse getChallengeMatch(@PathVariable Long id) {
        return ApiResponse.builder()
                .status(HttpStatus.FOUND.value())
                .data(challengeMatchService.getChallengeMatch(id))
                .message("challenge match")
                .build();
    }



}
