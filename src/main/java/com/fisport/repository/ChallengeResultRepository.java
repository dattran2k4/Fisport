package com.fisport.repository;

import com.fisport.model.ChallengeResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChallengeResultRepository extends JpaRepository<ChallengeResult, Long> {

}
