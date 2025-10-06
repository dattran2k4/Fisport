package com.Fisport.repository;

import com.Fisport.model.FieldType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FieldTypeRepository extends JpaRepository<FieldType,Long> {
    FieldType findBySlug(String slug);
}
