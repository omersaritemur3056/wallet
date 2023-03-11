package com.wallet.walletservice.service.impl;

import com.wallet.core.exception.EntityNotFoundException;
import com.wallet.core.model.Wallet;
import com.wallet.walletservice.exception.WalletBalanceUpdateException;
import com.wallet.walletservice.repository.WalletRepository;
import com.wallet.walletservice.service.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WalletServiceImpl implements WalletService {

    private final WalletRepository walletRepository;

    @Override
    public Wallet create(Wallet wallet) {
        return walletRepository.save(wallet);
    }

    @Override
    public void delete(Long id) {
        walletRepository.deleteById(id);
    }

    @Override
    public List<Wallet> findAllByUserId(Long userId) {
        return walletRepository.findAllByUserId(userId);
    }

    @Override
    public Wallet find(Long id) {
        return walletRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Wallet not found with id -> " + id));
    }

    @Override
    public void withdraw(Long id, BigDecimal amount) {
        var updatedWalletCount = walletRepository.withdraw(id, amount);
        if(updatedWalletCount.compareTo(0) <= 0) {
            throw new WalletBalanceUpdateException("Withdraw operation not succeeded for wallet id -> " + id);
        }
    }

    @Override
    public void deposit(Long id, BigDecimal amount) {
        var updatedWalletCount = walletRepository.deposit(id, amount);
        if(updatedWalletCount.compareTo(0) <= 0) {
            throw new WalletBalanceUpdateException("Deposit operation not succeeded for wallet id -> " + id);
        }
    }
}
