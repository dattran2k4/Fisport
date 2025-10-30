package com.fisport.repository;

import com.fisport.common.ESubFieldStatus;
import com.fisport.model.Field;
import com.fisport.model.SubField;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubFieldRepository extends JpaRepository<SubField, Long>, JpaSpecificationExecutor<SubField> {
    List<SubField> findAllByFieldId(Long FieldId);
    boolean existsByNameAndFieldId(String name, Long fieldId);
    Optional<SubField> findByName(String name);
    @Query(value = "SELECT s FROM SubField s WHERE " +
            "(:fieldId is NULL OR s.field.id = :fieldId)" +
            "AND (:status IS NULL OR s.status =:status)")
    List<SubField> findAllSubFields(@Param("fieldId") Long fieldId, @Param("status") ESubFieldStatus status);
    void deleteAllByField(Field field);
}
