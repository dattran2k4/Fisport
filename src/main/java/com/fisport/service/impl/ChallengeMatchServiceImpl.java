package com.fisport.service.impl;

import com.fisport.common.*;
import com.fisport.dto.request.ChallengeMatchRequest;
import com.fisport.dto.response.ChallengeMatchDetailResponse;
import com.fisport.dto.response.ChallengeMatchManagementResponse;
import com.fisport.dto.response.ChallengeMatchSummaryResponse;
import com.fisport.dto.response.PageResponse;
import com.fisport.exception.InvalidDataException;
import com.fisport.exception.ResourceNotFoundException;
import com.fisport.model.*;
import com.fisport.repository.*;
import com.fisport.service.*;
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
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Slf4j

public class ChallengeMatchServiceImpl implements ChallengeMatchService {

    private final ChallengeMatchRepository challengeMatchRepository;
    private final ChallengeMatchTypeService challengeMatchTypeService;
    private final ChallengeMatchTypeRepository challengeMatchTypeRepository;
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final ChallengeParticipantRepository challengeParticipantRepository;
    private final ChallengeParticipantService challengeParticipantService;
    private final WalletPaymentService walletPaymentService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createChallengeMatch(ChallengeMatchRequest request, String username) {
        log.info("create challenge match");
        User user = userRepository.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Booking booking = bookingRepository.findById(request.getBookingId()).orElseThrow(() -> new ResourceNotFoundException("Booking not found"));
        log.info("bookingId for match: ", booking.getId());

        if (!booking.getBookingStatus().equals(EBookingStatus.PAID)) {
            throw new InvalidDataException("Phải thanh toán booking trước");
        }

        ChallengeMatchType type = challengeMatchTypeRepository.findById(request.getChallengeMatchTypeId()).orElseThrow(() -> new ResourceNotFoundException("Challenge Match Type not found"));

        ChallengeMatch challengeMatch = ChallengeMatch.builder()
                .creator(user)
                .participationFee(request.getFee())
                .title(request.getTitle())
                .note(request.getNote())
                .status(EChallengeStatus.OPEN)
                .challengeMatchType(type)
                .suggestedLevel(request.getLevel())
                .booking(booking)
                .fieldTypeId(booking.getSubfield().getField().getFieldType().getId())
                .build();
        challengeMatchRepository.save(challengeMatch);

        booking.setChallengeMatch(challengeMatch);
        bookingRepository.save(booking);

        ChallengeParticipant participant = ChallengeParticipant.builder()
                .team(ETeam.TEAM_A)
                .paid(true)
                .paidAt(LocalDateTime.now())
                .user(user)
                .status(EParticipantStatus.ACCEPTED)
                .match(challengeMatch)
                .build();
        challengeParticipantRepository.save(participant);

        log.info("Created challengeId {}", challengeMatch.getId());
    }

    @Override
    public ChallengeMatchDetailResponse getChallengeMatchDetail(Long id) {
        ChallengeMatch challengeMatch = findChallengeMatch(id);
        User user = challengeMatch.getCreator();

        return ChallengeMatchDetailResponse.builder()
                .id(challengeMatch.getId())
                .title(challengeMatch.getTitle())
                .note(challengeMatch.getNote())
                .fee(challengeMatch.getParticipationFee())
                .level(challengeMatch.getSuggestedLevel())
                .sport(challengeMatch.getBooking().getSubfield().getField().getFieldType().getName())
                .city(challengeMatch.getBooking().getSubfield().getField().getWard().getCity().getName())
                .ward(challengeMatch.getBooking().getSubfield().getField().getWard().getName())
                .field(challengeMatch.getBooking().getSubfield().getField().getName())
                .subField(challengeMatch.getBooking().getSubfield().getName())
                .challengeStatus(challengeMatch.getStatus())
                .maxPlayers(challengeMatch.getChallengeMatchType().getMaxPlayers())
                .matchType(challengeMatch.getChallengeMatchType().getName())
                .currentPlayers(challengeParticipantService.getAcceptedCurrentPlayers(challengeMatch.getId()))
                .date(challengeMatch.getBooking().getBookingDate())
                .startTime(challengeMatch.getBooking().getStartTime())
                .endTime(challengeMatch.getBooking().getEndTime())
                .build();
    }

    @Override
    public PageResponse<ChallengeMatchSummaryResponse> getAllChallengeMatch(EChallengeStatus status, ELevel level,
                                                                            String matchType, LocalDate date, BigDecimal fee, Long cityId, Long fieldTypeId,
                                                                            int page, int size) {
        int pageNumber = (page > 0) ? page - 1 : 0;

        Specification<ChallengeMatch> spec = ChallengeMatchSpecification.filterChallengeMatch(status, level, matchType, date, fee, cityId, fieldTypeId);

        Pageable pageable = PageRequest.of(pageNumber, size, Sort.by(Sort.Direction.DESC, "booking.bookingDate"));

        Page<ChallengeMatch> matchPage = challengeMatchRepository.findAll(spec, pageable);

        List<ChallengeMatchSummaryResponse> response = matchPage.stream().map(m -> ChallengeMatchSummaryResponse.builder()
                .id(m.getId())
                .title(m.getTitle())
                .status(m.getStatus())
                .city(m.getBooking().getSubfield().getField().getWard().getCity().getName())
                .ward(m.getBooking().getSubfield().getField().getWard().getName())
                .fieldName(m.getBooking().getSubfield().getField().getName())
                .sport(m.getBooking().getSubfield().getField().getFieldType().getName())
                .currentPlayers(challengeParticipantService.getAcceptedCurrentPlayers(m.getId()))
                .matchType(m.getChallengeMatchType().getName())
                .date(m.getBooking().getBookingDate())
                .level(m.getSuggestedLevel().getValue())
                .maxPlayers(m.getChallengeMatchType().getMaxPlayers())
                .build()).toList();

        return PageResponse.<ChallengeMatchSummaryResponse>builder()
                .pageNumber(pageNumber)
                .pageSize(size)
                .totalElements(matchPage.getTotalElements())
                .totalPages(matchPage.getTotalPages())
                .data(response)
                .build();
    }

    @Override
    public ChallengeMatch findChallengeMatch(Long id) {
        return challengeMatchRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Challenge match not found"));
    }

    @Override
    public void updateMatchStatus(ChallengeMatch match) {
        Integer currentPlayers = challengeParticipantService.getAcceptedCurrentPlayers(match.getId());

        EChallengeStatus newStatus;
        if (currentPlayers == 0) {
            newStatus = EChallengeStatus.OPEN;
        } else if (currentPlayers < match.getChallengeMatchType().getMaxPlayers()) {
            newStatus = EChallengeStatus.PENDING;
        } else {
            newStatus = EChallengeStatus.FULL;
        }

        match.setStatus(newStatus);
        challengeMatchRepository.save(match);
    }

    @Override
    public void checkMatchFinished(Long matchId) {
        ChallengeMatch match = findChallengeMatch(matchId);
        if (match.getBooking().getEndTime().isBefore(LocalTime.now()) && match.getStatus().equals(EChallengeStatus.MATCHED)) {
            match.setStatus(EChallengeStatus.MATCHED);
            challengeMatchRepository.save(match);
        }
        throw new InvalidDataException("Trận đấu chưa hoàn thành");
    }

    @Override
    @Transactional
    public void cancelMatch(Long matchId) {
        ChallengeMatch match = findChallengeMatch(matchId);

        if (canCancel(matchId)) {

            //Refund
            if (match.getParticipants() != null) {
                for (ChallengeParticipant c : match.getParticipants()) {
                    Long playerId = c.getUser().getId();
                    walletPaymentService.refund(match.getCreator().getId(), playerId, match.getParticipationFee());
                }
            }
            match.setStatus(EChallengeStatus.CANCELLED);
            challengeMatchRepository.save(match);
            log.info("MatchId {} changed status {}", matchId, EChallengeStatus.CANCELLED);
        }
    }

    @Override
    public boolean canCancel(Long matchId) {
        ChallengeMatch challengeMatch = findChallengeMatch(matchId);

        LocalDate date = challengeMatch.getBooking().getBookingDate();
        LocalTime startTime = challengeMatch.getBooking().getStartTime();

        LocalDateTime start = LocalDateTime.of(date, startTime);

        EChallengeStatus status = challengeMatch.getStatus();

        if ((status.equals(EChallengeStatus.OPEN) || status.equals(EChallengeStatus.FULL) || status.equals(EChallengeStatus.PENDING))
                && LocalDateTime.now().plusHours(12).isBefore(start)) {
            return true;
        }

        return false;
    }

    @Override
    public List<ChallengeMatchManagementResponse> getListMatchForManagement(String username) {
        Optional<User> user = userService.findByUsername(username);

        List<ChallengeMatch> matches = challengeMatchRepository.findByCreatorId(user.get().getId());

        return matches.stream().map(m -> ChallengeMatchManagementResponse.builder()
                .id(m.getId())
                .title(m.getTitle())
                .status(m.getStatus())
                .maxPlayers(m.getChallengeMatchType().getMaxPlayers())
                .playersInMatch(challengeParticipantService.getAcceptedCurrentPlayers(m.getId()))
                .playersPending(challengeParticipantService.getPendingCurrentPlayers(m.getId()))
                .build()).toList();
    }
}
