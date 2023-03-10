package com.wallet.walletservice.service;

import com.wallet.core.model.WalletTransaction;

import java.util.List;

public interface WalletTransactionService {
    WalletTransaction create(WalletTransaction wallet);
    void delete(Long id);
    List<WalletTransaction> findAllDebitByWalletId(Long walletId);
    List<WalletTransaction> findAllCreditByWalletId(Long walletId);
}
