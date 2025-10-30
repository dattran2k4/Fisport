package com.fisport.repository;

import com.fisport.model.Feature;
import com.fisport.model.Field;
import com.fisport.model.FieldHasFeature;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface FieldHasFeatureRepository extends JpaRepository<FieldHasFeature, Long> {
    @Query("SELECT ff.feature FROM FieldHasFeature ff WHERE ff.field.id = :fieldId")
    Set<Feature> findFeaturesByFieldId(@Param("fieldId") Long fieldId);

    List<FieldHasFeature> findFieldHasFeatureByFieldId(Long fieldId);

    void deleteAllByField(Field field);
}
