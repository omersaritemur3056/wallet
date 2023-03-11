package com.wallet.walletservice.service.impl;

import com.wallet.core.model.WalletTransaction;
import com.wallet.walletservice.exception.InsufficientBalanceException;
import com.wallet.walletservice.exception.TransactionRequestNotValidException;
import com.wallet.walletservice.exception.WalletNotFoundException;
import com.wallet.walletservice.repository.WalletTransactionRepository;
import com.wallet.walletservice.service.WalletService;
import com.wallet.walletservice.service.WalletTransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

import static com.wallet.core.enums.TransactionStatus.COMPLETED;

@Slf4j
@RequiredArgsConstructor
@Service
public class WalletTransactionServiceImpl implements WalletTransactionService {

    private final WalletTransactionRepository walletTransactionRepository;
    private final WalletService walletService;

    @Transactional
    @Override
    public WalletTransaction create(WalletTransaction walletTransaction) {
        switch (walletTransaction.getTransactionType()) {
            case TRANSFER -> transfer();
            case WITHDRAW -> withdraw(walletTransaction);
            case DEPOSIT -> deposit(walletTransaction);
        }
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

    private void withdraw(WalletTransaction walletTransaction) {
        validateSenderRequest(walletTransaction);

        var walletId = walletTransaction.getSenderWallet().getId();
        var wallet = walletService.find(walletId);
        if(Objects.isNull(wallet)) {
            throw new WalletNotFoundException(String.format("Wallet not found with id -> [%s]", walletId));
        }

        var withdrawAmount = walletTransaction.getAmount();
        if(wallet.getBalance().compareTo(withdrawAmount) < 0) {
            throw new InsufficientBalanceException("Wallet balance is insufficient");
        }

        walletService.withdraw(walletId, withdrawAmount);
        log.info("Wallet withdraw operation succeeded with transactionId -> [{}]", walletTransaction.getTransactionId());
        walletTransaction.setTransactionStatus(COMPLETED);
    }

    private void deposit(WalletTransaction walletTransaction) {
        validateReceiverRequest(walletTransaction);

        var walletId = walletTransaction.getReceiverWallet().getId();
        var wallet = walletService.find(walletId);
        if(Objects.isNull(wallet)) {
            throw new WalletNotFoundException(String.format("Wallet not found with id -> [%s]", walletId));
        }

        walletService.deposit(walletId, walletTransaction.getAmount());
        log.info("Wallet deposit operation succeeded with transactionId -> [{}]", walletTransaction.getTransactionId());
        walletTransaction.setTransactionStatus(COMPLETED);
    }

    private void validateSenderRequest(WalletTransaction walletTransaction) {
        if(walletTransaction.getSenderWallet() == null
                || walletTransaction.getSenderWallet().getId() == null
                || walletTransaction.getAmount() == null
                || walletTransaction.getAmount().compareTo(BigDecimal.ZERO) < 0)  {
            throw new TransactionRequestNotValidException();
        }
    }

    private void validateReceiverRequest(WalletTransaction walletTransaction) {
        if(walletTransaction.getReceiverWallet() == null
                || walletTransaction.getReceiverWallet().getId() == null
                || walletTransaction.getAmount() == null
                || walletTransaction.getAmount().compareTo(BigDecimal.ZERO) < 0)  {
            throw new TransactionRequestNotValidException();
        }
    }

    private void transfer() {

    }
}
