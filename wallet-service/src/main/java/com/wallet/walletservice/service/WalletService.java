package com.wallet.walletservice.service;

import com.wallet.core.model.Wallet;

import java.util.List;

public interface WalletService {

    Wallet create(Wallet wallet);
    void delete(Long id);
    List<Wallet> findAllByUserId(Long id);
    Wallet find(Long id);

}
