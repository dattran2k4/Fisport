package com.fisport.repository;

import com.fisport.model.ChallengeMatchType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChallengeMatchTypeRepository extends JpaRepository<ChallengeMatchType, Long> {
}
