package com.wallet.walletservice.service;

import com.wallet.core.model.Wallet;

import java.math.BigDecimal;
import java.util.List;

public interface WalletService {

    Wallet create(Wallet wallet);
    void delete(Long id);
    List<Wallet> findAllByUserId(Long id);
    Wallet find(Long id);
    void withdraw(Long id, BigDecimal amount);
    void deposit(Long id, BigDecimal amount);
}
