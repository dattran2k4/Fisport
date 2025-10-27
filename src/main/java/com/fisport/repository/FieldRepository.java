package com.fisport.repository;

import com.fisport.model.Field;
import com.fisport.common.EFieldStatus;
import com.fisport.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FieldRepository extends JpaRepository<Field,Long>, JpaSpecificationExecutor<Field> {

    List<Field> findByOwnerId(Long ownerId);
    List<Field> findByFieldStatus(EFieldStatus fieldStatus);
    List<Field> findByFieldStatusAndOwner_Username(EFieldStatus fieldStatus, String userName);
    List<Field> findByOwner(User owner);

    Field findBySlug(String fieldNameSlug);

    //Bounding-box advanced calculating raidus
    @Query(value = "SELECT * FROM field f " +
            "WHERE f.latitude BETWEEN :minLat AND :maxLat " +
            "AND f.longitude BETWEEN :minLng AND :maxLng " +
            "AND (" +
            "6371 * acos(" +
            "cos(radians(:lat)) * cos(radians(f.latitude)) *" +
            "cos(radians(f.longitude) - radians(:lng)) + " +
            "sin(radians(:lat)) * sin(radians(f.latitude))" +
            ")" +
            ") <= :radius " +
            "AND f.status = 'ACTIVE'", nativeQuery = true)
    List<Field> findFieldWithinRadius(@Param("lat") Double lat,
                                                    @Param("lng") Double lng,
                                                    @Param("minLat") Double minLat,
                                                    @Param("maxLat") Double maxLat,
                                                    @Param("minLng") Double minLng,
                                                    @Param("maxLng") Double maxLng,
                                                    @Param("radius") Double radius);
}
