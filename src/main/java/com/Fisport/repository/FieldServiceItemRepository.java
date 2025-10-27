package com.Fisport.repository;

import com.Fisport.dto.response.FieldServiceItemResponse;
import com.Fisport.model.FieldServiceItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FieldServiceItemRepository extends JpaRepository<FieldServiceItem, Long> {

    @Query("SELECT new com.Fisport.dto.response.FieldServiceItemResponse(fsi.id, si.id, fsi.quantity, si.name, s.name, fsi.price)" +
            "FROM FieldServiceItem fsi " +
            "JOIN fsi.serviceItem si " +
            "JOIN si.service s " +
            "WHERE fsi.field.id = :fieldId AND fsi.status = 'ACTIVE'")
    List<FieldServiceItemResponse> findByActive(Long fieldId);

    @Query("SELECT fsi FROM FieldServiceItem fsi " +
            "JOIN FETCH fsi.field f " +
            "JOIN FETCH fsi.serviceItem si " +
            "JOIN FETCH si.service s " +
            "WHERE f.owner.id = :ownerId " +
            "ORDER BY f.name, s.name, si.name")
    List<FieldServiceItem> findAllByOwnerId(Long ownerId);
}
