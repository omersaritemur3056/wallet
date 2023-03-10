package com.wallet.walletservice.service.impl;

import com.wallet.core.model.WalletTransaction;
import com.wallet.walletservice.repository.WalletTransactionRepository;
import com.wallet.walletservice.service.WalletTransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WalletTransactionServiceImpl implements WalletTransactionService {

    private final WalletTransactionRepository walletTransactionRepository;

    @Override
    public WalletTransaction create(WalletTransaction walletTransaction) {
        return walletTransactionRepository.save(walletTransaction);
    }

    @Override
    public void delete(Long id) {
        walletTransactionRepository.deleteById(id);
    }

    @Override
    public List<WalletTransaction> findAllDebitByWalletId(Long walletId) {
        return walletTransactionRepository.findAllBySenderWalletId(walletId);
    }

    @Override
    public List<WalletTransaction> findAllCreditByWalletId(Long walletId) {
        return walletTransactionRepository.findAllByReceiverWalletId(walletId);
    }
}
