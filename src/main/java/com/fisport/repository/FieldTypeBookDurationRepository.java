package com.fisport.repository;

import com.fisport.model.Duration;
import com.fisport.model.FieldTypeBookDuration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FieldTypeBookDurationRepository extends JpaRepository<FieldTypeBookDuration,Long> {
    @Query("SELECT f.duration FROM FieldTypeBookDuration f WHERE f.fieldType.id = :id")
    List<Duration> findDurationByFieldTypeId(@Param("id") Long id);
}
