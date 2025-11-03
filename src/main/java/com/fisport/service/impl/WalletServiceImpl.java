package com.fisport.service.impl;

import com.fisport.common.ETransactionStatus;
import com.fisport.dto.response.WalletResponse;
import com.fisport.exception.InvalidDataException;
import com.fisport.exception.ResourceNotFoundException;
import com.fisport.model.Transaction;
import com.fisport.model.User;
import com.fisport.model.Wallet;
import com.fisport.repository.UserRepository;
import com.fisport.repository.WalletRepository;
import com.fisport.service.WalletService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

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
    public void debitWallet(Long walletId, Transaction transaction) {
        Wallet wallet = walletRepository.findById(walletId).orElseThrow(() -> new ResourceNotFoundException("Wallet not found"));

        if (wallet.getBalance().compareTo(transaction.getAmount()) < 0) {
            throw new InvalidDataException("Số dự không đủ");
        }

        wallet.setBalance(wallet.getBalance().subtract(transaction.getAmount()));
        walletRepository.save(wallet);

        log.info("Wallet debited: userId={}, txId={}, amount={}", wallet.getUser().getId(), transaction.getId(), transaction.getAmount());
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
