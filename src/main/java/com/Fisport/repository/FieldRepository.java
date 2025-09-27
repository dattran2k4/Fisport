package com.Fisport.repository;

import com.Fisport.model.Field;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FieldRepository extends JpaRepository<Field,Integer> {

    @Query("""
        SELECT f FROM Field f
        JOIN f.ward w
        WHERE (w.id = :wardId)
    """)
    List<Field> findByWardId(@Param("wardId") long wardId);
}
