package com.wallet.application.controller;

import com.wallet.core.rest.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/wallet")
public class WalletController {

    @GetMapping
    ResponseEntity<Response<String>> getAccountTransactions() {
        return ResponseEntity.ok(Response.success("abc"));
    }
}
