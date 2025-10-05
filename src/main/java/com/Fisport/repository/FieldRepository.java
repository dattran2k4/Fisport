package com.Fisport.repository;

import com.Fisport.model.Field;
import com.Fisport.common.EFieldStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FieldRepository extends JpaRepository<Field,Long> {

    List<Field> findByWardIdAndFieldTypeId(long wardId, long fieldTypeId);
    List<Field> findByFieldTypeId(long fieldTypeId);
    List<Field> findByOwnerId(Long ownerId);
    List<Field> findByFieldStatus(EFieldStatus fieldStatus);
    List<Field> findByFieldStatusAndOwner_Username(EFieldStatus fieldStatus, String userName);
}
