package com.wallet.walletservice.repository;

import com.wallet.core.model.WalletTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WalletTransactionRepository extends JpaRepository<WalletTransaction, Long> {
    List<WalletTransaction> findAllByReceiverWalletId(Long id);
    List<WalletTransaction> findAllBySenderWalletId(Long id);
}