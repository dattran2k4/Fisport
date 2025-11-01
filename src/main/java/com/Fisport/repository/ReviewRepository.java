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

    // Average rating for owner's fields
    @Query("SELECT AVG(r.rating) FROM Review r " +
            "WHERE (:ownerId IS NULL OR r.field.owner.id = :ownerId)")
    Double getAverageRating(@Param("ownerId") Long ownerId);

    // Average rating for specific field
    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.field.id = :fieldId")
    Double getAverageRatingByField(@Param("fieldId") Long fieldId);

    // Count reviews
    @Query("SELECT COUNT(r) FROM Review r " +
            "WHERE (:ownerId IS NULL OR r.field.owner.id = :ownerId)")
    Long countByOwnerId(@Param("ownerId") Long ownerId);
}
