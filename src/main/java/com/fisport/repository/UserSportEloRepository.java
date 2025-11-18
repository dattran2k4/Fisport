package com.fisport.repository;

import com.fisport.model.UserSportElo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserSportEloRepository extends JpaRepository<UserSportElo, Long> {
    Optional<UserSportElo> findByUserIdAndFieldTypeId(Long userId, Long fieldTypeId);


    @Query("SELECT u FROM UserSportElo u WHERE u.user.id IN :userIds AND u.fieldType.id = :sportId")
    List<UserSportElo> findByUserIdAndSport(@Param("userIds") List<Long> userIds, @Param("sportId") Long fieldTypeId);

    List<UserSportElo> findByUserUsername(String name);
}
