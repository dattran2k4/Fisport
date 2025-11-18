package com.fisport.repository;

import com.fisport.dto.response.FieldServiceItemResponse;
import com.fisport.model.FieldServiceItem;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FieldServiceItemRepository extends JpaRepository<FieldServiceItem, Long>, JpaSpecificationExecutor<FieldServiceItem> {

    @Query("SELECT new com.fisport.dto.response.FieldServiceItemResponse(fsi.id, si.id, fsi.quantity, si.name, s.name, fsi.price)" +
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

    @Query("SELECT fsi FROM FieldServiceItem fsi " +
            "JOIN FETCH fsi.serviceItem si " +
            "JOIN FETCH si.service s " +
            "WHERE fsi.field.id = :fieldId")
    List<FieldServiceItem> findByFieldId(Long fieldId);

    void deleteAllByField_Id(Long fieldId);

    @Query("SELECT fsi FROM FieldServiceItem fsi " +
            "JOIN FETCH fsi.serviceItem si " +
            "JOIN FETCH si.service s " +
            "WHERE fsi.field.id = :fieldId AND si.service.id = :serviceId")
    List<FieldServiceItem> findByFieldIdAndServiceId(Long fieldId, Long serviceId);

    @Query("SELECT fsi FROM FieldServiceItem fsi " +
            "JOIN FETCH fsi.serviceItem si " +
            "JOIN FETCH si.service s " +
            "WHERE fsi.field.id = :fieldId AND si.service.id = :serviceId AND fsi.status = 'ACTIVE'")
    List<FieldServiceItem> findActiveByFieldIdAndServiceId(Long fieldId, Long serviceId);

    @Query("SELECT fsi FROM FieldServiceItem fsi " +
            "JOIN FETCH fsi.serviceItem si " +
            "WHERE fsi.field.id = :fieldId AND fsi.serviceItem.id = :serviceItemId")
    Optional<FieldServiceItem> findByFieldIdAndServiceItemId(Long fieldId, Long serviceItemId);

}
