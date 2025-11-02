package com.fisport.repository;

import com.fisport.model.Booking;
import com.fisport.model.ChallengeMatch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChallengeMatchRepository extends JpaRepository<ChallengeMatch, Long>, JpaSpecificationExecutor<ChallengeMatch> {
    Optional<ChallengeMatch> findByBooking(Booking booking);

    List<ChallengeMatch> findByCreatorId(Long id);

}
