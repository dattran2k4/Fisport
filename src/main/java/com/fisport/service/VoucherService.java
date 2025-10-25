package com.fisport.service;

import com.fisport.dto.response.VoucherResponse;
import com.fisport.model.Voucher;

import java.math.BigDecimal;
import java.util.List;

public interface VoucherService {

    List<VoucherResponse> getVouchersByUserId(Long userId);

    List<VoucherResponse> findAllByActive();

    boolean isVoucherActive(String code);

    BigDecimal applyDiscount(BigDecimal totalPrice, Voucher voucher);
}
