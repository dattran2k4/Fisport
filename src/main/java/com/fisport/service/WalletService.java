package com.fisport.service;

import com.fisport.dto.response.WalletResponse;
import com.fisport.model.Transaction;

import java.math.BigDecimal;

public interface WalletService {

    void creditWallet(Transaction transaction);

    WalletResponse getWallet(Long id);

    void debitWallet(Long wardId, Transaction transaction);

    BigDecimal getBalanceByUser(String username);

    WalletResponse getWalletByUser(String username);
    
}
