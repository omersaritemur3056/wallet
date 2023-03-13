package com.wallet.walletservice.repository;

import com.wallet.core.model.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, Long> {
    List<Wallet> findAllByUserIdAndDeletedIsFalse(Long userId);

    @Modifying
    @Query("update Wallet w set w.balance = (w.balance - :amount), w.updatedAt = CURRENT_TIMESTAMP where w.id = :id")
    Integer withdraw(@Param("id") Long id, @Param("amount") BigDecimal amount);

    @Modifying
    @Query("update Wallet w set w.balance = (w.balance + :amount), w.updatedAt = CURRENT_TIMESTAMP where w.id = :id")
    Integer deposit(@Param("id") Long id, @Param("amount") BigDecimal amount);
}
