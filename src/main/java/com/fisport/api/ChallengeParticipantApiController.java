package com.fisport.api;

import com.fisport.dto.request.UpdateParticipantStatusRequest;
import com.fisport.dto.response.ApiResponse;
import com.fisport.service.ChallengeParticipantService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RequiredArgsConstructor
@RestController
@Validated
@RequestMapping("/api/v1/challenge-participants")
public class ChallengeParticipantApiController {

    private final ChallengeParticipantService challengeParticipantService;


    @GetMapping("/list")
    public ApiResponse getParticipantsList(@RequestParam Long matchId, Principal principal) {
        return ApiResponse.builder()
                .status(HttpStatus.FOUND.value())
                .message("participants list")
                .data(challengeParticipantService.getAllParticipantsByMatchAndCreator(matchId, principal.getName()))
                .build();
    }

    @PatchMapping("/{id}/accept")
    public ApiResponse acceptPlayer(@PathVariable Long id, @Valid @RequestBody UpdateParticipantStatusRequest request, Principal principal) {
        String user = challengeParticipantService.acceptPlayer(id, request, principal.getName());
        return ApiResponse.builder()
                .status(HttpStatus.ACCEPTED.value())
                .message("Chấp nhận người chơi " + user)
                .build();
    }

    @PatchMapping("/{id}/reject")
    public ApiResponse rejectPlayer(@PathVariable Long id, @Valid @RequestBody UpdateParticipantStatusRequest request, Principal principal) {
        String user = challengeParticipantService.rejectPlayer(id, request, principal.getName());
        return ApiResponse.builder()
                .status(HttpStatus.ACCEPTED.value())
                .message("Đã từ chối người chơi " + user)
                .build();
    }

    @PatchMapping("/{id}/not-shown")
    public ApiResponse rejectPlayer(@PathVariable Long id, Principal principal) {
        challengeParticipantService.markNotJoinPlayer(id, principal.getName());
        return ApiResponse.builder()
                .status(HttpStatus.ACCEPTED.value())
                .message("Đã đánh dấu không tham gia trận đấu!")
                .build();
    }



}
