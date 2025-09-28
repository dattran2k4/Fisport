package com.Fisport.repository;

import com.Fisport.model.Field;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FieldRepository extends JpaRepository<Field,Long> {

    List<Field> findByWardIdAndFieldTypeId(long wardId, long fieldTypeId);
    List<Field> findByFieldTypeId(long fieldTypeId);
    List<Field> findByOwnerId(Long ownerId);
}
