package com.fisport.repository;

import com.fisport.model.ServiceItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ServiceItemRepository extends JpaRepository<ServiceItem, Long> {
    
    List<ServiceItem> findByServiceId(Long serviceId);
    
    Optional<ServiceItem> findByNameAndServiceId(String name, Long serviceId);
}
