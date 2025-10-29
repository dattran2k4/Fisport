package com.fisport.repository;

import com.fisport.model.UserSportElo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserSportEloRepository extends JpaRepository<UserSportElo, Long> {
    Optional<UserSportElo> findByUserIdAndSportId(Long userId, Long sportId);

    List<UserSportElo> findByUserIdIn(List<Long> userIds);
}
