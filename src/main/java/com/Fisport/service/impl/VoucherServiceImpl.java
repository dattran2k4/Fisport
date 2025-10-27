package com.Fisport.service.impl;

import com.Fisport.common.EVoucherStatus;
import com.Fisport.dto.request.VoucherRequest;
import com.Fisport.dto.response.VoucherResponse;
import com.Fisport.exception.ResourceNotFoundException;
import com.Fisport.model.Voucher;
import com.Fisport.repository.VoucherRepository;
import com.Fisport.service.VoucherService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class VoucherServiceImpl implements VoucherService {
    private final VoucherRepository voucherRepository;


    @Override
    public List<VoucherResponse> getVouchersByUserId(Long userId) {
        List<Voucher> vouchers = voucherRepository.findByUsersId(userId);

        return vouchers.stream().map(this::toResponse).toList();
    }

    @Override
    public List<VoucherResponse> findAllByActive() {
        List<Voucher> vouchers = voucherRepository.findByStatus(EVoucherStatus.ACTIVE);
        return vouchers.stream().map(this::toResponse).toList();
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
        return vouchers.stream().map(this::toResponse).toList();
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
        Voucher voucher = voucherRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy voucher với ID: " + id));
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
        Voucher voucher = voucherRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy voucher với ID: " + id));
        
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
        Voucher voucher = voucherRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy voucher với ID: " + id));
        voucherRepository.delete(voucher);
    }

    private VoucherResponse toResponse(Voucher voucher) {
        return VoucherResponse.builder()
                .id(voucher.getId())
                .code(voucher.getCode())
                .description(voucher.getDescription())
                .discount(voucher.getDiscount())
                .limit(voucher.getLimit())
                .startDate(voucher.getStartDate())
                .endDate(voucher.getEndDate())
                .status(voucher.getStatus())
                .usedCount(voucher.getUsers() != null ? voucher.getUsers().size() : 0)
                .build();
    }
}
