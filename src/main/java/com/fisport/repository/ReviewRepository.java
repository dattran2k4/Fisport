package com.fisport.repository;

import com.fisport.model.Review;
import com.fisport.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends CrudRepository<Review, Long> {

    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.field.id = :fieldId")
    Double findAverageByFieldId(@Param("fieldId") Long fieldId);
    void deleteByFieldId(Long fieldId);
    List<Review> findByUser(User user);
}
