package com.fisport.repository;

import com.fisport.common.EVoucherStatus;
import com.fisport.model.Voucher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface VoucherRepository extends JpaRepository<Voucher, Long> {
    List<Voucher> findByUsersId(Long userId);

    @Modifying
    @Query("UPDATE Voucher v SET v.status = :inactiveStatus " +
            "WHERE v.status = :activeStatus AND v.endDate < :now")
    int expireVouchers(@Param("inactiveStatus") EVoucherStatus inactiveStatus,
                       @Param("activeStatus") EVoucherStatus activeStatus,
                       @Param("now") LocalDate now);

    List<Voucher> findByStatus(EVoucherStatus status);

    Optional<Voucher> findByCode(String code);
}
