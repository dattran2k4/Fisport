package com.fisport.service.impl;

import com.fisport.common.EVoucherStatus;
import com.fisport.dto.response.VoucherResponse;
import com.fisport.exception.InvalidDataException;
import com.fisport.exception.ResourceNotFoundException;
import com.fisport.model.Voucher;
import com.fisport.repository.VoucherRepository;
import com.fisport.service.VoucherService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j(topic = "VOUCHER-SERVICE")
public class VoucherServiceImpl implements VoucherService {
    private final VoucherRepository voucherRepository;


    @Override
    public List<VoucherResponse> getVouchersByUserId(String username) {
        List<Voucher> vouchers = voucherRepository.findByUsersUsername(username);

        return vouchers.stream().map(v -> VoucherResponse.builder()
                .id(v.getId())
                .discount(v.getDiscount())
                .limit(v.getLimit())
                .code(v.getCode())
                .description(v.getDescription())
                .startDate(v.getStartDate())
                .endDate(v.getEndDate())
                .status(v.getStatus())
                        .isActive(v.getStatus().equals((EVoucherStatus.ACTIVE))
                        && !LocalDate.now().isBefore(v.getStartDate())
                        && !LocalDate.now().isAfter(v.getEndDate()))
                .build())
                .filter(VoucherResponse::isActive)
                .toList();
    }

    @Override
    public List<VoucherResponse> findAllByActive() {
        List<Voucher> vouchers = voucherRepository.findByStatus(EVoucherStatus.ACTIVE);
        return vouchers.stream().map(f -> VoucherResponse.builder()
                .code(f.getCode())
                .description(f.getDescription())
                .startDate(f.getStartDate())
                .endDate(f.getEndDate())
                .limit(f.getLimit())
                .status(f.getStatus())
                .build()).toList();
    }

    @Override
    public boolean isVoucherActive(String code) {
        log.info("Check Active Voucher");
        LocalDate now = LocalDate.now();
        return voucherRepository.findByCode(code)
                .map(v -> v.getStatus() == EVoucherStatus.ACTIVE
                        && !now.isBefore(v.getStartDate())
                        && !now.isAfter(v.getEndDate()))
                .orElseThrow(() -> new InvalidDataException("Voucher is not valid"));
    }

    @Override
    public BigDecimal applyDiscount(BigDecimal totalPrice, Voucher voucher) {
        log.info("Apply discount Voucher");
        if (isVoucherActive(voucher.getCode()) && voucher.getDiscount() != null) {
            BigDecimal discountPercent = BigDecimal.valueOf(voucher.getDiscount()); // ví dụ 10, 20
            log.info("discount percent is {}", discountPercent);
            BigDecimal discountMultiplier = BigDecimal.valueOf(100)
                    .subtract(discountPercent)
                    .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
            totalPrice = totalPrice.multiply(discountMultiplier);
        }
        return totalPrice.setScale(0, RoundingMode.HALF_UP); // làm tròn về số nguyên nếu cần
    }

    private Voucher getVoucher(Long id) {
        return  voucherRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Không tìm thấy Voucher"));
    }

}
