package com.fisport.service.impl;

import com.fisport.common.EBookingStatus;
import com.fisport.common.EChallengeStatus;
import com.fisport.common.ELevel;
import com.fisport.common.EParticipantStatus;
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
import com.fisport.service.ChallengeMatchSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
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
                .participationFee(request.getFee())
                .title(request.getTitle())
                .note(request.getNote())
                .maxPlayers(request.getMaxPlayers())
                .status(EChallengeStatus.OPEN)
                .suggestedLevel(request.getLevel())
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
    public Page<ChallengeMatchResponse> getAllChallengeMatch(EChallengeStatus status, ELevel level,
                                                             Integer maxPlayers, LocalDate date, BigDecimal fee, Long cityId, Long fieldTypeId,
                                                             int page, int size) {
        int pageNumber = 0;
        if (page > 0) {
            pageNumber = page - 1;
        }

        Specification<ChallengeMatch> spec = ChallengeMatchSpecification.filterChallengeMatch(status, level, maxPlayers, date, fee, cityId, fieldTypeId);

        Pageable pageable = PageRequest.of(pageNumber, size, Sort.by(Sort.Direction.DESC, "booking.bookingDate"));

        return challengeMatchRepository.findAll(spec, pageable).map(this::toChallengeMatchResponse);
    }

    private ChallengeMatch findChallengeMatch(Long id) {
        return challengeMatchRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Challenge match not found"));
    }

    private ChallengeMatchResponse toChallengeMatchResponse(ChallengeMatch challengeMatch) {
        Integer currentPlayers = Math.toIntExact(challengeMatch.getParticipants().stream().filter(p -> p.getStatus() == EParticipantStatus.ACCEPTED).count());

        return ChallengeMatchResponse.builder()
                .id(challengeMatch.getId())
                .title(challengeMatch.getTitle())
                .note(challengeMatch.getNote())
                .fee(challengeMatch.getParticipationFee())
                .maxPlayers(challengeMatch.getMaxPlayers())
                .level(challengeMatch.getSuggestedLevel())
                .cityName(challengeMatch.getBooking().getSubfield().getField().getWard().getCity().getName())
                .wardName(challengeMatch.getBooking().getSubfield().getField().getWard().getName())
                .challengeStatus(challengeMatch.getStatus())
                .maxPlayers(challengeMatch.getMaxPlayers())
                .currentPlayers(currentPlayers)
                .date(challengeMatch.getBooking().getBookingDate())
                .startTime(challengeMatch.getBooking().getStartTime())
                .endTime(challengeMatch.getBooking().getEndTime())
                .createdAt(challengeMatch.getCreatedAt())
                .build();
    }

    @Override
    public void updateMatchStatus(ChallengeMatch match) {
        long acceptedCount = match.getParticipants().stream()
                .filter(p -> p.getStatus() == EParticipantStatus.ACCEPTED)
                .count();

        EChallengeStatus newStatus;
        if (acceptedCount == 0) {
            newStatus = EChallengeStatus.OPEN;
        } else if (acceptedCount < match.getMaxPlayers()) {
            newStatus = EChallengeStatus.PENDING;
        } else {
            newStatus = EChallengeStatus.FULL;
        }
        match.setStatus(newStatus);
        challengeMatchRepository.save(match);
    }
}
