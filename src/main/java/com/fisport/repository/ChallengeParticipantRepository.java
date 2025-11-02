package com.fisport.repository;

import com.fisport.common.EChallengeStatus;
import com.fisport.common.EParticipantStatus;
import com.fisport.common.ETeam;
import com.fisport.dto.response.ChallengeParticipantsInfoResponse;
import com.fisport.model.ChallengeParticipant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChallengeParticipantRepository extends JpaRepository<ChallengeParticipant, Long> {
    Optional<ChallengeParticipant> findByUserIdAndMatchId(Long userId, Long matchId);


    @Query("SELECT new com.fisport.dto.response.ChallengeParticipantsInfoResponse(cp.id, u.username, use.level, cp.team, use.elo, u.gender) " +
            "FROM ChallengeParticipant cp " +
            "JOIN cp.user u " +
            "JOIN cp.match m " +
            "JOIN UserSportElo use " +
            "ON use.user.id = u.id " +
            "AND use.fieldType.id = m.fieldTypeId " +
            "WHERE cp.status = 'ACCEPTED' AND m.id = :matchId")
    List<ChallengeParticipantsInfoResponse> findAllAcceptedParticipantsInfo(Long matchId);

    @Query("SELECT p FROM ChallengeParticipant p JOIN FETCH p.match m WHERE p.user.username = :username")
    List<ChallengeParticipant> findAllWithMatchByUser(@Param("username") String username);

    boolean existsByMatchIdAndUserId(Long matchId, Long id);

    List<ChallengeParticipant> findByMatchIdAndTeamAndStatus(Long matchId, ETeam team, EParticipantStatus status);


    Long countByMatchIdAndTeam(Long matchId, ETeam eTeam);

    @Query("SELECT COUNT(p) FROM ChallengeParticipant p WHERE p.match.id = :matchId AND p.team = :team AND p.status = 'ACCEPTED'")
    Integer countAcceptedPlayersByTeam(@Param("matchId") Long matchId, @Param("team") ETeam team);

    List<ChallengeParticipant> findAllByMatchId(Long id);
}
