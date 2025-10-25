package com.Fisport.service;

import com.Fisport.dto.response.VoucherResponse;
import com.Fisport.model.Voucher;

import java.math.BigDecimal;
import java.util.List;

public interface VoucherService {

    List<VoucherResponse> getVouchersByUserId(Long userId);

    List<VoucherResponse> findAllByActive();

    boolean isVoucherActive(String code);

    BigDecimal applyDiscount(BigDecimal totalPrice, Voucher voucher);
}
