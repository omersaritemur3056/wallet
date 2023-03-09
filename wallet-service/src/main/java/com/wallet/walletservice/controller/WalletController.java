package com.wallet.walletservice.controller;

import com.wallet.core.rest.Response;
import lombok.RequiredArgsConstructor;
import org.apache.commons.text.RandomStringGenerator;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/wallets")
public class WalletController {

    @GetMapping
    ResponseEntity<Response<String>> getTransactions() {
        var generator = new RandomStringGenerator.Builder()
                .withinRange('0', 'Z')
                .filteredBy(Character::isLetterOrDigit)
                .build();
        var randomLetters = "SC-" + generator.generate(8).toUpperCase();
        return ResponseEntity.ok(Response.success(randomLetters));
    }
}
