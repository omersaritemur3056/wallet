package com.wallet.walletservice.service;

public interface AuthenticationService {
    String login(String email, String password);

    void verify(String token);
}
