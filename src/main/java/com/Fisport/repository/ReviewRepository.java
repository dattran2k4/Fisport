package com.Fisport.repository;

import com.Fisport.model.Review;
import com.Fisport.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends CrudRepository<Review, Long> {

    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.field.id = :fieldId")
    Double findAverageByFieldId(@Param("fieldId") Long fieldId);

    @Query("SELECT AVG(r.rating) FROM Review r")
    Double findGlobalAverageRating();

    java.util.List<Review> findTop5ByOrderByCreatedAtDesc();

    void deleteByFieldId(Long fieldId);
    List<Review> findByUser(User user);
}
