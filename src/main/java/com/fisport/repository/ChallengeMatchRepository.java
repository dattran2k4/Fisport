package com.fisport.repository;

import com.fisport.model.ChallengeMatch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChallengeMatchRepository extends JpaRepository<ChallengeMatch, Long> {
}
