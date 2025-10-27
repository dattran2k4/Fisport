package com.Fisport.service;

import com.Fisport.dto.request.VoucherRequest;
import com.Fisport.dto.response.VoucherResponse;
import com.Fisport.model.Voucher;

import java.util.List;

public interface VoucherService {
    List<VoucherResponse> getVouchersByUserId(Long userId);
    List<VoucherResponse> findAllByActive();
    
    // Owner CRUD operations
    List<VoucherResponse> getAllVouchers();
    VoucherResponse getVoucherById(Long id);
    VoucherResponse createVoucher(VoucherRequest request);
    VoucherResponse updateVoucher(Long id, VoucherRequest request);
    void deleteVoucher(Long id);
    void updateExpiredVouchers();
}
