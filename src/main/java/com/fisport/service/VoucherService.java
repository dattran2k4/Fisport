package com.fisport.service;

import com.fisport.dto.request.VoucherRequest;
import com.fisport.dto.response.VoucherResponse;
import com.fisport.model.Voucher;

import java.math.BigDecimal;
import java.util.List;

public interface VoucherService {
    List<VoucherResponse> getVouchersByUserId(Long userId);
    List<VoucherResponse> findAllByActive();
    BigDecimal applyDiscount(BigDecimal total, Voucher voucher);
    
    // Owner CRUD operations
    List<VoucherResponse> getAllVouchers();
    VoucherResponse getVoucherById(Long id);
    VoucherResponse createVoucher(VoucherRequest request);
    VoucherResponse updateVoucher(Long id, VoucherRequest request);
    void deleteVoucher(Long id);
    void updateExpiredVouchers();
}
