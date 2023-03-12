package com.wallet.walletservice.controller;

import com.wallet.core.model.Wallet;
import com.wallet.core.rest.Response;
import com.wallet.walletservice.service.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/wallets")
@CrossOrigin(origins = "http://localhost:3000")
public class WalletController {

    private final WalletService walletService;

    @GetMapping("/list/{userId}")
    ResponseEntity<Response<List<Wallet>>> findAllByUserId(@PathVariable Long userId) {
        var wallets = walletService.findAllByUserId(userId);
        return ResponseEntity.ok(Response.success(wallets));
    }

    @GetMapping("/{id}")
    ResponseEntity<Response<Wallet>> find(@PathVariable Long id) {
        var wallet = walletService.find(id);
        return ResponseEntity.ok(Response.success(wallet));
    }

    @PostMapping
    ResponseEntity<Response<Wallet>> create(@RequestBody Wallet wallet) {
        var responseWallet = walletService.create(wallet);
        return ResponseEntity.ok(Response.success(responseWallet));
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Response<Void>> delete(@PathVariable Long id) {
        walletService.delete(id);
        return new ResponseEntity<>(HttpStatus.RESET_CONTENT);
    }
}
