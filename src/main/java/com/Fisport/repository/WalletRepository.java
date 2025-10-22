package com.Fisport.repository;

import com.Fisport.model.User;
import com.Fisport.model.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, Long> {
    Wallet findByUser(User user);
}
