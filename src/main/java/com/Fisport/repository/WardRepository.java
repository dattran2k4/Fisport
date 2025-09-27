package com.Fisport.repository;

import com.Fisport.model.Ward;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WardRepository extends CrudRepository<Ward,Integer> {
    List<Ward> findByCityId(long cityId);
}
