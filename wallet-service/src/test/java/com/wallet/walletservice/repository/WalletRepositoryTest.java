package com.wallet.walletservice.repository;

import com.wallet.core.model.Wallet;
import com.wallet.walletservice.helper.WalletNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;

import java.math.BigDecimal;
import java.util.Date;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@TestPropertySource(value = "classpath:application-test.properties")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
class WalletRepositoryTest {

    @Autowired
    private WalletRepository walletRepository;

    @Test
    @Rollback
    void givenWalletIdAndAmount_whenWithdraw_thenUpdatedCountIsGreaterThanZero() {
        // given - precondition or setup
        var wallet = new Wallet();
        wallet.setId(1L);
        wallet.setName(WalletNameGenerator.generate());
        wallet.setCreatedAt(new Date());
        wallet.setBalance(BigDecimal.valueOf(100));
        var savedWallet = walletRepository.save(wallet);

        // when - action or the behaviour that we are going test
        Integer updatedWalletCount = walletRepository.withdraw(savedWallet.getId(), BigDecimal.valueOf(100));

        // then - verify the output
        assertThat(updatedWalletCount).isEqualTo(1);
    }

    @Test
    @Rollback
    void givenWalletIdAndAmount_whenDeposit_thenUpdatedCountIsGreaterThanZero() {
        // given - precondition or setup
        var wallet = new Wallet();
        wallet.setId(1L);
        wallet.setName(WalletNameGenerator.generate());
        wallet.setCreatedAt(new Date());
        wallet.setBalance(BigDecimal.valueOf(100));
        var savedWallet = walletRepository.save(wallet);

        // when - action or the behaviour that we are going test
        var updatedWalletCount = walletRepository.deposit(savedWallet.getId(), BigDecimal.valueOf(100));

        // then - verify the output
        assertThat(updatedWalletCount).isEqualTo(1);
    }

}