package com.Fisport.service;

import com.Fisport.dto.response.WalletResponse;
import com.Fisport.model.Transaction;
import com.Fisport.model.Wallet;

import java.math.BigDecimal;

public interface WalletService {

    void creditWallet(Transaction transaction);

    WalletResponse getWallet(Long id);

    void debitWallet(Long wardId, Transaction transaction);

    BigDecimal getBalanceByUser(String username);

    WalletResponse getWalletByUser(String username);
    
}
