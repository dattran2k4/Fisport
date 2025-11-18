package com.fisport.service.impl;

import com.fisport.common.EVoucherStatus;
import com.fisport.dto.request.VoucherRequest;
import com.fisport.dto.response.VoucherResponse;
import com.fisport.exception.ResourceNotFoundException;
import com.fisport.model.Voucher;
import com.fisport.repository.VoucherRepository;
import com.fisport.service.VoucherService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class VoucherServiceImpl implements VoucherService {
    private final VoucherRepository voucherRepository;

    @Override
    public BigDecimal applyDiscount(BigDecimal total, Voucher voucher) {
        if (total == null || voucher == null) {
            return total;
        }

        if (voucher.getStatus() != EVoucherStatus.ACTIVE) {
            return total;
        }

        java.time.LocalDate now = java.time.LocalDate.now();
        if ((voucher.getStartDate() != null && now.isBefore(voucher.getStartDate()))
                || (voucher.getEndDate() != null && now.isAfter(voucher.getEndDate()))) {
            return total;
        }

        Integer discountPercent = voucher.getDiscount();
        if (discountPercent == null || discountPercent <= 0) {
            return total;
        }

        int applied = Math.min(discountPercent, 100);
        BigDecimal discountMultiplier = BigDecimal.valueOf(100 - applied)
                .divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP);

        return total.multiply(discountMultiplier).setScale(2, RoundingMode.HALF_UP);
    }


    @Override
    public List<VoucherResponse> getVouchersByUserId(Long userId) {
        List<Voucher> vouchers = voucherRepository.findByUsersId(userId);

        // Chuyển đổi từ Voucher sang VoucherResponse
        List<VoucherResponse> result = new ArrayList<>();
        for (Voucher voucher : vouchers) {
            VoucherResponse response = toResponse(voucher);
            result.add(response);
        }
        return result;
    }

    @Override
    public List<VoucherResponse> findAllByActive() {
        List<Voucher> vouchers = voucherRepository.findByStatus(EVoucherStatus.ACTIVE);

        // Chuyển đổi từ Voucher sang VoucherResponse
        List<VoucherResponse> result = new ArrayList<>();
        for (Voucher voucher : vouchers) {
            VoucherResponse response = toResponse(voucher);
            result.add(response);
        }
        return result;
    }

    @Override
    @Transactional
    public List<VoucherResponse> getAllVouchers() {
        // Tự động cập nhật vouchers hết hạn trước khi lấy danh sách
        java.time.LocalDate now = java.time.LocalDate.now();
        voucherRepository.expireVouchers(
                EVoucherStatus.INACTIVE,
                EVoucherStatus.ACTIVE,
                now
        );

        List<Voucher> vouchers = voucherRepository.findAll();

        // Chuyển đổi từ Voucher sang VoucherResponse
        List<VoucherResponse> result = new ArrayList<>();
        for (Voucher voucher : vouchers) {
            VoucherResponse response = toResponse(voucher);
            result.add(response);
        }
        return result;
    }

    @Override
    @Transactional
    public void updateExpiredVouchers() {
        java.time.LocalDate now = java.time.LocalDate.now();
        voucherRepository.expireVouchers(
                EVoucherStatus.INACTIVE,
                EVoucherStatus.ACTIVE,
                now
        );
    }

    @Override
    @Transactional(readOnly = true)
    public VoucherResponse getVoucherById(Long id) {
        java.util.Optional<Voucher> voucherOptional = voucherRepository.findById(id);
        if (!voucherOptional.isPresent()) {
            throw new ResourceNotFoundException("Không tìm thấy voucher với ID: " + id);
        }
        Voucher voucher = voucherOptional.get();
        return toResponse(voucher);
    }

    @Override
    @Transactional
    public VoucherResponse createVoucher(VoucherRequest request) {
        Voucher voucher = Voucher.builder()
                .code(request.getCode().toUpperCase())
                .description(request.getDescription())
                .discount(request.getDiscount())
                .limit(request.getLimit())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .status(EVoucherStatus.ACTIVE)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        Voucher savedVoucher = voucherRepository.save(voucher);
        return toResponse(savedVoucher);
    }

    @Override
    @Transactional
    public VoucherResponse updateVoucher(Long id, VoucherRequest request) {
        java.util.Optional<Voucher> voucherOptional = voucherRepository.findById(id);
        if (!voucherOptional.isPresent()) {
            throw new ResourceNotFoundException("Không tìm thấy voucher với ID: " + id);
        }
        Voucher voucher = voucherOptional.get();

        voucher.setCode(request.getCode().toUpperCase());
        voucher.setDescription(request.getDescription());
        voucher.setDiscount(request.getDiscount());
        voucher.setLimit(request.getLimit());
        voucher.setStartDate(request.getStartDate());
        voucher.setEndDate(request.getEndDate());
        voucher.setUpdatedAt(LocalDateTime.now());

        Voucher updatedVoucher = voucherRepository.save(voucher);
        return toResponse(updatedVoucher);
    }

    @Override
    @Transactional
    public void deleteVoucher(Long id) {
        java.util.Optional<Voucher> voucherOptional = voucherRepository.findById(id);
        if (!voucherOptional.isPresent()) {
            throw new ResourceNotFoundException("Không tìm thấy voucher với ID: " + id);
        }
        Voucher voucher = voucherOptional.get();
        voucherRepository.delete(voucher);
    }

    private VoucherResponse toResponse(Voucher voucher) {
        // Tính số lượng user đã sử dụng voucher
        int usedCount = 0;
        if (voucher.getUsers() != null) {
            usedCount = voucher.getUsers().size();
        }

        // Tạo VoucherResponse
        return VoucherResponse.builder()
                .id(voucher.getId())
                .code(voucher.getCode())
                .description(voucher.getDescription())
                .discount(voucher.getDiscount())
                .limit(voucher.getLimit())
                .startDate(voucher.getStartDate())
                .endDate(voucher.getEndDate())
                .status(voucher.getStatus())
                .usedCount(usedCount)
                .build();
    }
}
