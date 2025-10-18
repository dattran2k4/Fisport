package com.Fisport.service.impl;

import com.Fisport.dto.response.VoucherResponse;
import com.Fisport.model.Voucher;
import com.Fisport.repository.VoucherRepository;
import com.Fisport.service.VoucherService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class VoucherServiceImpl implements VoucherService {
    private final VoucherRepository voucherRepository;


    @Override
    public List<VoucherResponse> getVouchersByUserId(Long userId) {
        List<Voucher> vouchers = voucherRepository.findByUsersId(userId);

        return vouchers.stream().map(f -> VoucherResponse.builder()
                .discount(f.getDiscount())
                .limit(f.getLimit())
                .code(f.getCode())
                .description(f.getDescription())
                .startDate(f.getStartDate())
                .endDate(f.getEndDate())
                .build()).toList();
    }
}
