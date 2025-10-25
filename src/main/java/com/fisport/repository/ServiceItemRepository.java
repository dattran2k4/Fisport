package com.fisport.repository;

import com.fisport.model.ServiceItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiceItemRepository extends JpaRepository<ServiceItem, Long> , JpaSpecificationExecutor<ServiceItem> {
    List<ServiceItem> findByName(String name);
}
