package com.Fisport.repository;

import com.Fisport.model.Review;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends CrudRepository<Review, Long> {

    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.field.id = :fieldId")
    Double findAverageByFieldId(@Param("fieldId") Long fieldId);
}
