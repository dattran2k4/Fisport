package com.fisport.service.impl;

import com.fisport.common.*;
import com.fisport.dto.request.ChallengeMatchCreateRequest;
import com.fisport.dto.request.ChallengeMatchUpdateRequest;
import com.fisport.dto.response.*;
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
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class ChallengeMatchServiceImpl implements ChallengeMatchService {

    private final ChallengeMatchRepository challengeMatchRepository;
    private final ChallengeMatchTypeRepository challengeMatchTypeRepository;
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ChallengeParticipantRepository challengeParticipantRepository;
    private final ChallengeParticipantService challengeParticipantService;
    private final WalletPaymentService walletPaymentService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createChallengeMatch(ChallengeMatchCreateRequest request, String username) {
        log.info("Create challenge match");
        User user = userRepository.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Booking booking = bookingRepository.findById(request.getBookingId()).orElseThrow(() -> new ResourceNotFoundException("Booking not found"));
        log.info("bookingId {}: for match", booking.getId());

        if (!booking.getBookingStatus().equals(EBookingStatus.PAID)) {
            throw new InvalidDataException("Phải thanh toán booking trước");
        }

        if (booking.getChallengeMatch() != null) {
            throw new InvalidDataException("Đã tạo trận đấu này từ trước rồi!");
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
                .fieldTypeId(booking.getSubfield().getField().getFieldType().getId())
                .build();

        challengeMatch.setBooking(booking);
        challengeMatch = challengeMatchRepository.save(challengeMatch);

        booking.setChallengeMatch(challengeMatch);
        bookingRepository.save(booking);
        log.info("bookingId {} set match {}", booking.getId(), booking.getChallengeMatch().toString());

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
    @Transactional(rollbackFor = Exception.class)
    public void updateChallengeMatch(Long id, ChallengeMatchUpdateRequest request, String username) {
        ChallengeMatch match = findChallengeMatch(id);

        ChallengeMatchType type = challengeMatchTypeRepository.findById(request.getChallengeMatchTypeId()).orElseThrow(() -> new ResourceNotFoundException("Challenge Match Type not found"));

        List<ChallengeParticipant> participants = challengeParticipantRepository.findAllByMatchId(id);

        if (!username.equals(match.getCreator().getUsername())) {
            throw new InvalidDataException("Bạn không được phép thực hiện");
        }

        boolean hasPaid = participants.stream()
                .anyMatch(p -> !p.getUser().getId().equals(match.getCreator().getId())   // không tính creator
                        && Boolean.TRUE.equals(p.isPaid()));                         // đã trả phí
        if (hasPaid && request.getFee() != null && !request.getFee().equals(match.getParticipationFee())) {
            throw new InvalidDataException("Không thể thay đổi phí vì đã có người chơi thanh toán.");
        }

        match.setChallengeMatchType(type);
        match.setTitle(request.getTitle());
        match.setNote(request.getNote());
        if (request.getFee() != null && !request.getFee().equals(match.getParticipationFee())) {
            match.setParticipationFee(request.getFee());
        }

        challengeMatchRepository.save(match);
        log.info("Updated challengeMatchId {}", match.getId());
    }

    @Override
    public ChallengeMatchDetailResponse getChallengeMatchDetail(Long id) {
        ChallengeMatch challengeMatch = findChallengeMatch(id);
        User user = challengeMatch.getCreator();

        return ChallengeMatchDetailResponse.builder()
                .id(challengeMatch.getId())
                .title(challengeMatch.getTitle())
                .creator(user.getUsername())
                .gender(user.getGender())
                .challengeStatus(challengeMatch.getStatus())
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
                                                                            Long typeId, Long cityId, Long fieldTypeId,
                                                                            int page, int size) {
        int pageNumber = (page > 0) ? page - 1 : 0;

        Specification<ChallengeMatch> spec = ChallengeMatchSpecification.filterChallengeMatch(status, level, typeId, cityId, fieldTypeId);

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
    @Transactional
    public void checkMatchFinished(Long matchId) {
        ChallengeMatch match = findChallengeMatch(matchId);
        if (LocalDateTime.now().isAfter(LocalDateTime.of(match.getBooking().getBookingDate(), match.getBooking().getEndTime())) && !match.getStatus().equals(EChallengeStatus.MATCHED)) {
            match.setStatus(EChallengeStatus.MATCHED);
            challengeMatchRepository.save(match);

            log.info("MatchId {} is MATCHED", matchId);
        }
        throw new InvalidDataException("Trận đấu chưa hoàn thành");
    }

    @Override
    @Transactional
    public void cancelMatch(Long matchId, String username) {
        ChallengeMatch match = findChallengeMatch(matchId);

        if (!username.equals(match.getCreator().getUsername())) {
            throw new InvalidDataException("Bạn không được phép thực hiện");
        }

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
        } else {
            throw new InvalidDataException("Không thể hủy trận đấu");
        }
    }

    @Override
    public boolean canCancel(Long matchId) {
        ChallengeMatch challengeMatch = findChallengeMatch(matchId);

        LocalDate date = challengeMatch.getBooking().getBookingDate();
        LocalTime startTime = challengeMatch.getBooking().getStartTime();

        LocalDateTime start = LocalDateTime.of(date, startTime);

        log.info("date={}, time={}, start={}", date, startTime, start);

        EChallengeStatus status = challengeMatch.getStatus();

        //Disable 12 hours
        if ((status.equals(EChallengeStatus.OPEN) || status.equals(EChallengeStatus.FULL) || status.equals(EChallengeStatus.PENDING))
                && LocalDateTime.now().plusHours(12).isBefore(start)) {
            return true;
        }

        return false;
    }

    @Override
    public List<ChallengeMatchManagementResponse> getListMatchForManagement(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException("User not found"));

        List<ChallengeMatch> matches = challengeMatchRepository.findByCreatorId(user.getId());

        return matches.stream().map(m -> ChallengeMatchManagementResponse.builder()
                .id(m.getId())
                .title(m.getTitle())
                .status(m.getStatus())
                .playerTeamA(challengeParticipantService.countAcceptedPlayersByTeam(m.getId(), ETeam.TEAM_A))
                .playerTeamB(challengeParticipantService.countAcceptedPlayersByTeam(m.getId(), ETeam.TEAM_B))
                .maxPlayersPerTeam(m.getChallengeMatchType().getMaxPlayers() / 2)
                .matchType(m.getChallengeMatchType().getName())
                .date(m.getBooking().getBookingDate())
                .sport(m.getChallengeMatchType().getFieldType().getName())
                .actions(checkAction(m))
                .build()).toList();
    }

    @Override
    public ChallengeMatchDetailManagementResponse getMatchDetailForManagement(Long matchId, String username) {
        ChallengeMatch match = findChallengeMatch(matchId);

        if (!match.getCreator().getUsername().equals(username)) {
            throw new InvalidDataException("Bạn không được phép thực hiện");
        }

        return ChallengeMatchDetailManagementResponse.builder()
                .id(match.getId())
                .title(match.getTitle())
                .note(match.getNote())
                .challengeStatus(match.getStatus())
                .matchType(match.getChallengeMatchType().getName())
                .level(match.getSuggestedLevel())
                .date(match.getBooking().getBookingDate())
                .city(match.getBooking().getSubfield().getField().getWard().getCity().getName())
                .ward(match.getBooking().getSubfield().getField().getWard().getName())
                .field(match.getBooking().getSubfield().getField().getName())
                .subField(match.getBooking().getSubfield().getName())
                .startTime(match.getBooking().getStartTime())
                .endTime(match.getBooking().getEndTime())
                .fee(match.getParticipationFee())
                .playerTeamA(challengeParticipantService.countAcceptedPlayersByTeam(match.getId(), ETeam.TEAM_A))
                .playerTeamB(challengeParticipantService.countAcceptedPlayersByTeam(match.getId(), ETeam.TEAM_B))
                .maxPlayersPerTeam(match.getChallengeMatchType().getMaxPlayers() / 2)
                .matchType(match.getChallengeMatchType().getName())
                .sport(match.getChallengeMatchType().getFieldType().getName())
                .createdAt(match.getCreatedAt())
                .updatedAt(match.getUpdatedAt())
                .build();

    }

    private List<String> checkAction(ChallengeMatch m) {
        List<String> actions = new ArrayList<>();
        switch (m.getStatus()) {
            case OPEN:
            case PENDING:
            case FULL:
                actions.add("View");
                actions.add("Edit");
                actions.add("Cancel");
                break;
            case CANCELLED:
                break;
            case MATCHED:
                actions.add("View Result");
                break;
            default:
                break;
        }
        return actions;
    }
}
