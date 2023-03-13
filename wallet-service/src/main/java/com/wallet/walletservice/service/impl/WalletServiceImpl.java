package com.wallet.walletservice.service.impl;

import com.wallet.core.exception.EntityNotFoundException;
import com.wallet.core.model.User;
import com.wallet.core.model.Wallet;
import com.wallet.walletservice.exception.TransactionRequestNotValidException;
import com.wallet.walletservice.exception.WalletBalanceUpdateException;
import com.wallet.walletservice.exception.WalletDeleteException;
import com.wallet.walletservice.helper.WalletNameGenerator;
import com.wallet.walletservice.repository.WalletRepository;
import com.wallet.walletservice.service.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class WalletServiceImpl implements WalletService {

    private final WalletRepository walletRepository;

    @Override
    public Wallet create(Wallet wallet) {
        if(Objects.isNull(wallet.getName())) {
            wallet.setName(WalletNameGenerator.generate());
        }

        if(Objects.isNull(wallet.getBalance())) {
            wallet.setBalance(BigDecimal.ZERO);
        }

        if(wallet.getBalance().compareTo(BigDecimal.ZERO) < 0) {
            throw new TransactionRequestNotValidException();
        }

        return walletRepository.save(wallet);
    }

    @Override
    public void delete(Long id) {
        var wallet = getWallet(id);
        if(wallet.getBalance().compareTo(BigDecimal.ZERO) > 0) {
            throw new WalletDeleteException("Delete operation cannot be performed because the wallet has a balance. id -> " + id);
        }

        wallet.setDeleted(true);
        walletRepository.save(wallet);
    }

    @Override
    public List<Wallet> findAllByUserId(Long userId) {
        return walletRepository.findAllByUserIdAndDeletedIsFalse(userId);
    }

    @Override
    public Wallet find(Long id) {
        return getWallet(id);
    }

    private Wallet getWallet(Long id) {
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
