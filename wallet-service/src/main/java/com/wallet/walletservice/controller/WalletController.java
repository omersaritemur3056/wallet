package com.wallet.walletservice.controller;

import com.wallet.core.rest.Response;
import lombok.RequiredArgsConstructor;
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
        Response<String> abc = Response.success("abc");
        return ResponseEntity.ok(abc);
    }
}
