package com.fisport.repository;

import com.fisport.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    Optional<User> findByPhone(String phoneNumber);

    @Query(value = "SELECT u FROM User u WHERE " +
            "lower(u.username) like lower(concat('%', :keyword, '%'))" +
            "or lower(u.email) like lower(concat('%', :keyword, '%'))" +
            "or lower(u.phone) like lower(concat('%', :keyword, '%'))")
    List<User> searchByKeyword(String keyword);
}
