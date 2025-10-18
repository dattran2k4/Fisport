package com.Fisport.repository;

import com.Fisport.model.FieldHasTimeSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;
import java.util.List;

@Repository
public interface FieldHasTimeSlotRepository extends JpaRepository<FieldHasTimeSlot,Long> {
    List<FieldHasTimeSlot> findByFieldId(Long id);

    @Query("SELECT fts FROM FieldHasTimeSlot fts JOIN fts.timeSlot ts WHERE " +
            "fts.field.id = ?1 AND ts.startTime >= ?2 AND ts.startTime < ?3 " +
            "ORDER BY ts.startTime ASC")
    List<FieldHasTimeSlot> findSlotsForBooking(Long fieldId, LocalTime start, LocalTime end);
}
