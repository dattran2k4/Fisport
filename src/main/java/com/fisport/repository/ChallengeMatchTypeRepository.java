package com.fisport.repository;

import com.fisport.model.ChallengeMatchType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChallengeMatchTypeRepository extends JpaRepository<ChallengeMatchType, Long> {
    @Query("SELECT c FROM ChallengeMatchType c WHERE c.fieldType.id = :fieldTypeId")
    List<ChallengeMatchType> findByFieldTypeId(@Param("fieldTypeId") Long fieldTypeId);
}
