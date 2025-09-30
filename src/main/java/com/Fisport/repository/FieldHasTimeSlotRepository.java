package com.Fisport.repository;

import com.Fisport.model.FieldHasTimeSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FieldHasTimeSlotRepository extends JpaRepository<FieldHasTimeSlot,Long> {
    List<FieldHasTimeSlot> findByFieldId(Long id);
}
