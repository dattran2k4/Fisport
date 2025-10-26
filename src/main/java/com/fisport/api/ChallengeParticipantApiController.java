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

    private ChallengeParticipantService challengeParticipantService;

    @PutMapping("/{id}/response/")
    public ApiResponse responseForMatch(@PathVariable Long id, @Valid @RequestBody UpdateParticipantStatusRequest request, Principal principal) {
        challengeParticipantService.responeMatch(id, request, principal.getName());
        return ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message("OK")
                .build();
    }
}
