package com.Fisport.service.impl;

import com.Fisport.common.ETransactionStatus;
import com.Fisport.dto.response.WalletResponse;
import com.Fisport.exception.ResourceNotFoundException;
import com.Fisport.model.Transaction;
import com.Fisport.model.User;
import com.Fisport.model.Wallet;
import com.Fisport.repository.UserRepository;
import com.Fisport.repository.WalletRepository;
import com.Fisport.service.WalletService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.security.Principal;

@Slf4j
@RequiredArgsConstructor
@Service
public class WalletServiceImpl implements WalletService {

    private final WalletRepository walletRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public void creditWallet(Transaction transaction) {

        if (transaction.getStatus() != ETransactionStatus.PENDING) {
            log.warn("Transaction {} already processed with status {}", transaction.getId(), transaction.getStatus());
            return;
        }

        Wallet wallet = transaction.getWallet();

        wallet.setBalance(wallet.getBalance().add(transaction.getAmount()));

        walletRepository.save(wallet);

        log.info("WalletId {} credited: userId={}, txId={}, amount={}", wallet.getId(), wallet.getUser().getId(), transaction.getId(), transaction.getAmount());
    }

    @Override
    public WalletResponse getWallet(Long id) {
        Wallet wallet = findWallet(id);
        return WalletResponse.builder()
                .id(id)
                .balance(wallet.getBalance())
                .createdAt(wallet.getCreatedAt())
                .updatedAt(wallet.getUpdatedAt())
                .build();
    }

    private Wallet findWallet(Long id) {
        return walletRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Wallet not found"));
    }

    @Override
    @Transactional
    public void debitWallet(Long wardId, Transaction transaction) {
        Wallet wallet = walletRepository.findById(wardId).orElseThrow(() -> new ResourceNotFoundException("Wallet not found"));
        wallet.setBalance(wallet.getBalance().subtract(transaction.getAmount()));
        walletRepository.save(wallet);

        log.info("Wallet credited: userId={}, txId={}, amount={}",  wallet.getUser().getId(), transaction.getId(), transaction.getAmount());
    }

    @Override
    public BigDecimal getBalanceByUser(String username) {

        User user = userRepository.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return user.getWallet().getBalance();
    }

    @Override
    public WalletResponse getWalletByUser(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Wallet wallet = user.getWallet();
        return getWallet(wallet.getId());
    }
}
