package com.wallet.walletservice.controller;

import com.wallet.core.model.WalletTransaction;
import com.wallet.core.rest.Response;
import com.wallet.walletservice.service.WalletTransactionService;
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

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/transactions")
@CrossOrigin(origins = "http://localhost:3000")
public class WalletTransactionController {

    private final WalletTransactionService walletTransactionService;

    @GetMapping("/{walletId}")
    ResponseEntity<Response<List<WalletTransaction>>> findAllByWalletId(@PathVariable Long walletId) {
        var walletDebitTransactions = walletTransactionService.findAllDebitByWalletId(walletId);
        var walletCreditTransactions = walletTransactionService.findAllCreditByWalletId(walletId);

        List<WalletTransaction> walletTransactions =
                Stream.of(walletDebitTransactions, walletCreditTransactions)
                .flatMap(Collection::stream)
                .sorted(Comparator.comparing(WalletTransaction::getCreatedAt).reversed())
                .toList();

        return ResponseEntity.ok(Response.success(walletTransactions));
    }

    @PostMapping
    ResponseEntity<Response<WalletTransaction>> create(@RequestBody WalletTransaction walletTransaction) {
        var walletTransactionResponse = walletTransactionService.create(walletTransaction);
        return ResponseEntity.ok(Response.success(walletTransactionResponse));
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Response<Void>> delete(@PathVariable Long id) {
        walletTransactionService.delete(id);
        return new ResponseEntity<>(HttpStatus.RESET_CONTENT);
    }
}
