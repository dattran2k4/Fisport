package com.fisport.service;

import com.fisport.common.EVoucherStatus;
import com.fisport.repository.VoucherRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@RequiredArgsConstructor
@Service
public class VoucherScheduler {

    private final VoucherRepository voucherRepository;
    private static final Logger logger = LoggerFactory.getLogger(VoucherScheduler.class);

    @Scheduled(cron = "0 0 0 * * *") // chạy mỗi ngày lúc 00:00
    @Transactional
    public void updateExpiredVouchers() {
        int updatedCount = voucherRepository.expireVouchers(
                EVoucherStatus.INACTIVE,
                EVoucherStatus.ACTIVE,
                LocalDate.now()
        );
        logger.info("Đã cập nhật {} voucher hết hạn", updatedCount);
    }
}
