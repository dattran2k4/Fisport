package com.fisport.api;

import com.fisport.common.EChallengeStatus;
import com.fisport.common.ELevel;
import com.fisport.dto.request.ChallengeMatchCreateRequest;
import com.fisport.dto.request.JoinMatchRequest;
import com.fisport.dto.response.ApiResponse;
import com.fisport.service.ChallengeMatchService;
import com.fisport.service.ChallengeParticipantService;
import jakarta.validation.Valid;
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
    private final ChallengeParticipantService challengeParticipantService;

    @PostMapping("/create")
    public ApiResponse createChallengeMatch(@RequestBody ChallengeMatchCreateRequest request, Principal principal) {
        challengeMatchService.createChallengeMatch(request, principal.getName());
        return ApiResponse.builder()
                .status(HttpStatus.CREATED.value())
                .message("Tạo trân đấu thành công!")
                .build();
    }

    @GetMapping
    public ApiResponse getAllChallengeMatches(@RequestParam(required = false) EChallengeStatus status,
                                              @RequestParam(required = false) ELevel level,
                                              @RequestParam(required = false) String matchType,
                                              @RequestParam(required = false) LocalDate date,
                                              @RequestParam(required = false) BigDecimal fee,
                                              @RequestParam(required = false) Long cityId,
                                              @RequestParam(required = false) Long fieldTypeId,
                                              @RequestParam(defaultValue = "0") int page,
                                              @RequestParam(defaultValue = "10") int size) {

        return ApiResponse.builder()
                .status(HttpStatus.FOUND.value())
                .message("challenge matches")
                .data(challengeMatchService.getAllChallengeMatch(status, level, matchType, date, fee, cityId, fieldTypeId, page, size))
                .build();
    }

    @GetMapping("/list-by")
    public ApiResponse getChallengeMatches(Principal principal) {
        return ApiResponse.builder()
                .status(HttpStatus.FOUND.value())
                .message("challenge matches")
                .data(challengeMatchService.getListMatchForManagement(principal.getName()))
                .build();
    }

    @GetMapping("/{id}/detail")
    public ApiResponse getChallengeMatch(@PathVariable Long id) {
        return ApiResponse.builder()
                .status(HttpStatus.FOUND.value())
                .data(challengeMatchService.getChallengeMatchDetail(id))
                .message("challenge match")
                .build();
    }

    //Web detail
    @GetMapping("/{id}/participants-accepted")
    public ApiResponse getParticipantsInMatch(@PathVariable Long id) {
        return ApiResponse.builder()
                .status(HttpStatus.FOUND.value())
                .message("participants in match")
                .data(challengeParticipantService.getAllAcceptedParticipantsInfo(id))
                .build();
    }



    @PostMapping("/{id}/join")
    public ApiResponse joinMatch(@PathVariable Long id, @Valid @RequestBody JoinMatchRequest request, Principal principal) {
        challengeParticipantService.joinMatch(id, request, principal.getName());
        return ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message("Đã yêu cầu tham gia, đợi creator phản hồi")
                .build();
    }

    @PutMapping("/{id}/update-result")
    public ApiResponse updateResultMatch(@PathVariable Long id, Principal principal) {
        return ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message("OK")
                .build();
    }

    @PostMapping("/{id}/cancel")
    public ApiResponse cancelMatch(@PathVariable Long id, Principal principal) {
        challengeMatchService.cancelMatch(id, principal.getName());
        return ApiResponse.builder()
                .status(200)
                .message("Đã hủy trận đấu và hoàn trả giao dịch cho các người chơi!")
                .build();
    }

}
