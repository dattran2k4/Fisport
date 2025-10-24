package com.Fisport.repository;

import com.Fisport.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import com.Fisport.common.ERole;
import com.Fisport.common.EUserStatus;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    Optional<User> findByPhone(String phoneNumber);

    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    boolean existsByPhone(String phoneNumber);

    @Query(value = "SELECT u FROM User u WHERE " +
        "lower(u.username) like lower(concat('%', :keyword, '%')) " +
        "or lower(u.email) like lower(concat('%', :keyword, '%')) " +
        "or lower(u.phone) like lower(concat('%', :keyword, '%'))")
    List<User> searchByKeyword(String keyword);

    @Query("SELECT u FROM User u WHERE " +
        "(:keyword IS NULL OR :keyword = '' OR lower(u.username) like lower(concat('%', :keyword, '%')) " +
        "OR lower(u.email) like lower(concat('%', :keyword, '%')) " +
        "OR lower(u.phone) like lower(concat('%', :keyword, '%'))) " +
        "AND (:role IS NULL OR u.role.name = :role) " +
        "AND (:status IS NULL OR u.status = :status)")
    Page<User> findByFilters(@Param("keyword") String keyword,
                 @Param("role") ERole role,
                 @Param("status") EUserStatus status,
                 Pageable pageable);

    long count();

    long countByStatus(EUserStatus status);

    @org.springframework.data.jpa.repository.Query("SELECT COUNT(u) FROM User u WHERE u.createdAt >= :from")
    long countCreatedAfter(java.time.LocalDateTime from);
}
