package com.fisport.repository;

import com.fisport.common.ESubFieldStatus;
import com.fisport.model.SubField;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface SubFieldRepository extends JpaRepository<SubField, Long> {
    boolean existsByNameAndFieldId(String name, Long fieldId);
    Optional<SubField> findByName(String name);
    @Query(value = "SELECT s FROM SubField s WHERE " +
            "(:fieldId is NULL OR s.field.id = :fieldId)" +
            "AND (:status IS NULL OR s.status =:status)")
    List<SubField> findAllSubFields(@Param("fieldId") Long fieldId, @Param("status") ESubFieldStatus status);

    @Query("SELECT sf FROM SubField sf " +
                  "JOIN sf.field f " +
                  "JOIN f.fieldType ft " +
                  "JOIN f.ward w " +
                  "WHERE ft.slug = :sportSlug " +
                  "  AND w.slug = :districtSlug " +
                  "  AND f.fieldStatus = 'ACTIVE' " +
                  "  AND sf.status = 'AVAILABLE' " +
                  "  AND f.openTime <= :time AND f.closeTime > :time " +
                  "  AND NOT EXISTS (" +
                  "    SELECT b FROM Booking b " +
                  "    WHERE b.subfield.id = sf.id " +
                  "    AND b.bookingDate = :date " +
                  "    AND b.startTime < :time " +
                  "    AND b.endTime > :time" +
                  ")")
    List<SubField> findAvailableSubFields(
            @Param("sportSlug") String sportSlug,
            @Param("districtSlug") String districtSlug,
            @Param("date") LocalDate date,
            @Param("time") LocalTime time
    );
}
