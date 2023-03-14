package com.wallet.walletservice.exception;

public class WalletTransactionNotFoundException extends RuntimeException {
    public WalletTransactionNotFoundException(String message) {
        super(message);
    }
}
