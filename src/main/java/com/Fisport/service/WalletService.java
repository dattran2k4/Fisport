package com.Fisport.service;

import com.Fisport.model.Transaction;
import com.Fisport.model.Wallet;

public interface WalletService {

    void creditWallet(Transaction transaction);

    Wallet getWallet(Long id);

    void debitWallet(Long wardId, Transaction transaction);
}
