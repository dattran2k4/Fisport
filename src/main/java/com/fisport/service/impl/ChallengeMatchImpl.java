package com.fisport.service.impl;

import com.fisport.common.EBookingStatus;
import com.fisport.common.EChallengeStatus;
import com.fisport.dto.request.ChallengeMatchRequest;
import com.fisport.dto.response.ChallengeMatchResponse;
import com.fisport.dto.response.FieldTypeResponse;
import com.fisport.exception.ResourceNotFoundException;
import com.fisport.model.Booking;
import com.fisport.model.ChallengeMatch;
import com.fisport.model.User;
import com.fisport.repository.BookingRepository;
import com.fisport.repository.ChallengeMatchRepository;
import com.fisport.repository.UserRepository;
import com.fisport.service.BookingService;
import com.fisport.service.ChallengeMatchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j

public class ChallengeMatchImpl implements ChallengeMatchService {

    private final ChallengeMatchRepository challengeMatchRepository;
    private final BookingService bookingService;
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public String createChallengeMatch(ChallengeMatchRequest request, String username) {

        User user = userRepository.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException("User not found"));

        String paymentToken = bookingService.createBooking(request.getBookingRequest(), user.getId());

        Booking booking = bookingRepository.findByPaymentTokenAndBookingStatus(paymentToken, List.of(EBookingStatus.PENDING, EBookingStatus.FAILED)).orElseThrow(() -> new ResourceNotFoundException("Booking not found or paid already"));
        log.info("Find bookingId {}",  booking.getId());

        ChallengeMatch challengeMatch = ChallengeMatch.builder()
                .creator(user)
                .startTime(request.getBookingRequest().getStartTime())
                .participationFee(request.getFee())
                .endTime(request.getBookingRequest().getEndTime())
                .title(request.getTitle())
                .note(request.getNote())
                .maxPlayers(request.getMaxPlayers())
                .status(EChallengeStatus.OPEN)
                .suggestedLevel(request.getLevel())
                .date(request.getBookingRequest().getDate())
                .booking(booking)
                .build();

        challengeMatchRepository.save(challengeMatch);
        log.info("Created challengeId {}",  challengeMatch.getId());
        return paymentToken;
    }

    @Override
    public ChallengeMatchResponse getChallengeMatch(Long id) {
        ChallengeMatch challengeMatch = findChallengeMatch(id);
        return toChallengeMatchResponse(challengeMatch);
    }

    @Override
    public Page<ChallengeMatchResponse> getAllChallengeMatch() {
        return List.of();
    }

    private ChallengeMatch findChallengeMatch(Long id) {
        return challengeMatchRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Challenge match not found"));
    }

    private ChallengeMatchResponse toChallengeMatchResponse(ChallengeMatch challengeMatch) {
        return ChallengeMatchResponse.builder()
                .id(challengeMatch.getId())
                .title(challengeMatch.getTitle())
                .note(challengeMatch.getNote())
                .maxPlayers(challengeMatch.getMaxPlayers())
                .level(challengeMatch.getSuggestedLevel())
                .fieldTypeResponse(FieldTypeResponse.builder()
                        .id(challengeMatch.getFieldType().getId())
                        .name(challengeMatch.getFieldType().getName())
                        .slug(challengeMatch.getFieldType().getSlug())
                        .build())
                .createdAt(challengeMatch.getCreatedAt())
                .build();
    }
}
