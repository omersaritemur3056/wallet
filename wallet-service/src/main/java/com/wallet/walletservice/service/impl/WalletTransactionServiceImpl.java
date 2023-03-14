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
        if(walletTransaction == null || walletTransaction.getTransactionType() == null) {
            throw new TransactionRequestNotValidException();
        }

        switch (walletTransaction.getTransactionType()) {
            case TRANSFER -> doTransfer(walletTransaction);
            case WITHDRAW -> doWithdraw(walletTransaction);
            case DEPOSIT -> doDeposit(walletTransaction);
        }
        return walletTransactionRepository.save(walletTransaction);
    }

    @Override
    public List<WalletTransaction> findAllDebitByWalletId(Long walletId) {
        return walletTransactionRepository.findAllBySenderWalletId(walletId);
    }

    @Override
    public List<WalletTransaction> findAllCreditByWalletId(Long walletId) {
        return walletTransactionRepository.findAllByReceiverWalletId(walletId);
    }

    private void doWithdraw(WalletTransaction walletTransaction) {
        validateSenderWallet(walletTransaction);
        validateAmount(walletTransaction.getAmount());

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

    private void doDeposit(WalletTransaction walletTransaction) {
        validateReceiverWallet(walletTransaction);
        validateAmount(walletTransaction.getAmount());

        var walletId = walletTransaction.getReceiverWallet().getId();
        var wallet = walletService.find(walletId);
        if(Objects.isNull(wallet)) {
            throw new WalletNotFoundException(String.format("Wallet not found with id -> [%s]", walletId));
        }

        walletService.deposit(walletId, walletTransaction.getAmount());
        log.info("Wallet deposit operation succeeded with transactionId -> [{}]", walletTransaction.getTransactionId());
        walletTransaction.setTransactionStatus(COMPLETED);
    }

    private void doTransfer(WalletTransaction walletTransaction) {
        validateSenderWallet(walletTransaction);
        validateReceiverWallet(walletTransaction);
        validateAmount(walletTransaction.getAmount());

        var senderWalletId = walletTransaction.getSenderWallet().getId();
        var senderWallet = walletService.find(senderWalletId);
        if(Objects.isNull(senderWallet)) {
            throw new WalletNotFoundException(String.format("Sender wallet not found with id -> [%s]", senderWalletId));
        }

        var receiverWalletId = walletTransaction.getReceiverWallet().getId();
        var receiverWallet = walletService.find(receiverWalletId);
        if(Objects.isNull(receiverWallet)) {
            throw new WalletNotFoundException(String.format("Receiver Wallet not found with id -> [%s]", receiverWalletId));
        }

        var transferAmount = walletTransaction.getAmount();
        if(senderWallet.getBalance().compareTo(transferAmount) < 0) {
            throw new InsufficientBalanceException("Wallet balance is insufficient");
        }

        walletService.withdraw(senderWalletId, transferAmount);
        walletService.deposit(receiverWalletId, transferAmount);

        log.info("Transfer operation succeeded with transactionId -> [{}]", walletTransaction.getTransactionId());
        walletTransaction.setTransactionStatus(COMPLETED);
    }

    private void validateSenderWallet(WalletTransaction walletTransaction) {
        if(walletTransaction.getSenderWallet() == null || walletTransaction.getSenderWallet().getId() == null)  {
            throw new TransactionRequestNotValidException();
        }
    }

    private void validateReceiverWallet(WalletTransaction walletTransaction) {
        if(walletTransaction.getReceiverWallet() == null || walletTransaction.getReceiverWallet().getId() == null)  {
            throw new TransactionRequestNotValidException();
        }
    }

    private void validateAmount(BigDecimal amount) {
        if(amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new TransactionRequestNotValidException();
        }
    }
}
