package com.wallet.walletservice.service.impl;

import com.wallet.core.exception.EntityNotFoundException;
import com.wallet.core.model.Wallet;
import com.wallet.walletservice.exception.TransactionRequestNotValidException;
import com.wallet.walletservice.exception.WalletBalanceUpdateException;
import com.wallet.walletservice.exception.WalletDeleteException;
import com.wallet.walletservice.helper.WalletNameGenerator;
import com.wallet.walletservice.repository.WalletRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class WalletServiceImplTest {
    @InjectMocks
    private WalletServiceImpl walletService;
    @Mock
    private WalletRepository walletRepository;
    @Test
    void givenWallet_whenCreate_thenReturnSuccess() {
        // given - precondition or setup
        var expectedWallet = new Wallet();
        expectedWallet.setId(1L);
        expectedWallet.setName(WalletNameGenerator.generate());
        expectedWallet.setCreatedAt(new Date());
        expectedWallet.setBalance(BigDecimal.ONE);
        given(walletRepository.save(any())).willReturn(expectedWallet);

        // when - action or the behaviour that we are going test
        var actualWallet = walletService.create(expectedWallet);

        // then - verify the output
        assertNotNull(actualWallet);
        assertNotNull(actualWallet.getId());
        assertNull(expectedWallet.getUpdatedAt());
        assertEquals(expectedWallet.getId(), actualWallet.getId());
        assertEquals(expectedWallet.getName(), actualWallet.getName());
        assertEquals(expectedWallet.getCreatedAt(), actualWallet.getCreatedAt());
        assertEquals(expectedWallet.getUser(), actualWallet.getUser());
        assertEquals(expectedWallet.getBalance(), actualWallet.getBalance());
        verify(walletRepository).save(any());
    }

    @Test
    void givenWalletWithoutName_whenCreate_thenReturnSuccess() {
        // given - precondition or setup
        var expectedWallet = new Wallet();
        expectedWallet.setId(1L);
        expectedWallet.setCreatedAt(new Date());
        expectedWallet.setBalance(BigDecimal.ONE);
        given(walletRepository.save(any())).willReturn(expectedWallet);

        // when - action or the behaviour that we are going test
        var actualWallet = walletService.create(expectedWallet);

        // then - verify the output
        assertNotNull(actualWallet);
        assertNotNull(actualWallet.getId());
        assertNull(expectedWallet.getUpdatedAt());
        assertEquals(expectedWallet.getId(), actualWallet.getId());
        assertEquals(expectedWallet.getCreatedAt(), actualWallet.getCreatedAt());
        assertEquals(expectedWallet.getUser(), actualWallet.getUser());
        assertEquals(expectedWallet.getBalance(), actualWallet.getBalance());
        verify(walletRepository).save(any());
    }

    @Test
    void givenWalletWithBalanceIsNull_whenCreate_thenReturnSuccess() {
        // given - precondition or setup
        var expectedWallet = new Wallet();
        expectedWallet.setId(1L);
        expectedWallet.setName(WalletNameGenerator.generate());
        expectedWallet.setCreatedAt(new Date());
        expectedWallet.setBalance(null);
        given(walletRepository.save(any())).willReturn(expectedWallet);

        // when - action or the behaviour that we are going test
        var actualWallet = walletService.create(expectedWallet);

        // then - verify the output
        assertNotNull(actualWallet);
        assertNotNull(actualWallet.getId());
        assertNull(expectedWallet.getUpdatedAt());
        assertEquals(expectedWallet.getId(), actualWallet.getId());
        assertEquals(expectedWallet.getName(), actualWallet.getName());
        assertEquals(expectedWallet.getCreatedAt(), actualWallet.getCreatedAt());
        assertEquals(expectedWallet.getUser(), actualWallet.getUser());
        assertEquals(BigDecimal.ZERO, actualWallet.getBalance());
        verify(walletRepository).save(any());
    }

    @Test
    void givenWalletWithBalanceIsEqualAndLessThanZero_whenCreate_thenThrowWalletEntityNotFoundException() {
        // given - precondition or setup
        var expectedWallet = new Wallet();
        expectedWallet.setId(1L);
        expectedWallet.setName(WalletNameGenerator.generate());
        expectedWallet.setCreatedAt(new Date());
        expectedWallet.setBalance(BigDecimal.ONE.negate());

        // when - action or the behaviour that we are going test
        Executable actual = () -> walletService.create(expectedWallet);

        // then - verify the output
        assertThrows(TransactionRequestNotValidException.class, actual);
        verify(walletRepository, times(0)).save(any());
    }

    @Test
    void givenWalletId_whenDelete_thenReturnSuccess() {
        // given - precondition or setup
        var expectedWallet = new Wallet();
        expectedWallet.setId(1L);
        expectedWallet.setName(WalletNameGenerator.generate());
        expectedWallet.setCreatedAt(new Date());
        expectedWallet.setBalance(BigDecimal.ZERO);
        given(walletRepository.findById(anyLong())).willReturn(Optional.of(expectedWallet));

        // when - action or the behaviour that we are going test
        walletService.delete(anyLong());

        // then - verify the output
        assertTrue(expectedWallet.isDeleted());
        verify(walletRepository).save(any());
    }

    @Test
    void givenWalletId_whenDelete_thenThrowWalletEntityNotFoundException() {
        // given - precondition or setup
        given(walletRepository.findById(anyLong())).willReturn(Optional.empty());

        // when - action or the behaviour that we are going test
        Executable actual = () -> walletService.delete(anyLong());

        // then - verify the output
        assertThrows(EntityNotFoundException.class, actual);
        verify(walletRepository, times(0)).deleteById(anyLong());
    }

    @Test
    void givenWalletId_whenDeleteBalanceIsPositive_thenThrowWalletEntityNotFoundException() {
        // given - precondition or setup
        var expectedWallet = new Wallet();
        expectedWallet.setId(1L);
        expectedWallet.setName(WalletNameGenerator.generate());
        expectedWallet.setCreatedAt(new Date());
        expectedWallet.setBalance(BigDecimal.ONE);
        given(walletRepository.findById(anyLong())).willReturn(Optional.of(expectedWallet));

        // when - action or the behaviour that we are going test
        Executable actual = () -> walletService.delete(anyLong());

        // then - verify the output
        assertThrows(WalletDeleteException.class, actual);
        verify(walletRepository, times(0)).deleteById(anyLong());
    }

    @Test
    void givenUserId_whenFindAllByUserId_thenReturnSuccess() {
        // given - precondition or setup
        var expectedWalletFirst = new Wallet();
        expectedWalletFirst.setId(1L);
        var expectedWalletSecond = new Wallet();
        expectedWalletSecond.setId(2L);
        var expectedWallets = List.of(expectedWalletFirst, expectedWalletSecond);
        given(walletRepository.findAllByUserIdAndDeletedIsFalse(anyLong())).willReturn(expectedWallets);

        // when - action or the behaviour that we are going test
        var wallets = walletService.findAllByUserId(anyLong());

        // then - verify the output
        assertNotNull(wallets);
        assertFalse(wallets.isEmpty());
        assertEquals(2, wallets.size());
        assertTrue(wallets.stream().anyMatch(wallet -> wallet.getId() == 1L));
        assertTrue(wallets.stream().anyMatch(wallet -> wallet.getId() == 2L));
        verify(walletRepository).findAllByUserIdAndDeletedIsFalse(anyLong());
    }

    @Test
    void givenWalletIdWithDeletedFalse_whenFind_thenReturnSuccess() {
        // given - precondition or setup
        var expectedWalletFirst = new Wallet();
        expectedWalletFirst.setId(1L);
        expectedWalletFirst.setDeleted(false);
        given(walletRepository.findById(anyLong())).willReturn(Optional.of(expectedWalletFirst));

        // when - action or the behaviour that we are going test
        var wallet = walletService.find(anyLong());

        // then - verify the output
        assertNotNull(wallet);
        assertNotNull(wallet.getId());
        assertFalse(wallet.isDeleted());
        verify(walletRepository).findById(anyLong());
    }

    @Test
    void givenWalletIdWithDeletedTrue_whenFind_thenReturnSuccess() {
        // given - precondition or setup
        var expectedWalletFirst = new Wallet();
        expectedWalletFirst.setId(1L);
        expectedWalletFirst.setDeleted(true);
        given(walletRepository.findById(anyLong())).willReturn(Optional.of(expectedWalletFirst));

        // when - action or the behaviour that we are going test
        var wallet = walletService.find(anyLong());

        // then - verify the output
        assertNotNull(wallet);
        assertNotNull(wallet.getId());
        assertTrue(wallet.isDeleted());
        verify(walletRepository).findById(anyLong());
    }

    @Test
    void givenWalletIdWithoutDeleted_whenFind_thenReturnSuccess() {
        // given - precondition or setup
        var expectedWalletFirst = new Wallet();
        expectedWalletFirst.setId(1L);
        given(walletRepository.findById(anyLong())).willReturn(Optional.of(expectedWalletFirst));

        // when - action or the behaviour that we are going test
        var wallet = walletService.find(anyLong());

        // then - verify the output
        assertNotNull(wallet);
        assertNotNull(wallet.getId());
        assertFalse(wallet.isDeleted());
        verify(walletRepository).findById(anyLong());
    }

    @Test
    void givenWalletIdAndAmount_whenWithdraw_thenReturnSuccess() {
        // given - precondition or setup
        var expectedWalletFirst = new Wallet();
        expectedWalletFirst.setId(1L);
        given(walletRepository.withdraw(anyLong(), any())).willReturn(1);

        // when - action or the behaviour that we are going test
        walletService.withdraw(anyLong(), any());

        // then - verify the output
        verify(walletRepository).withdraw(anyLong(), any());
    }

    @Test
    void givenWalletIdAndAmount_whenWithdraw_thenThrowWalletBalanceUpdateException() {
        // given - precondition or setup
        var amount  = BigDecimal.ONE;
        var expectedWalletFirst = new Wallet();
        expectedWalletFirst.setId(1L);

        // when - action or the behaviour that we are going test
        Executable actual = () -> walletService.withdraw(expectedWalletFirst.getId(), amount);

        // then - verify the output
        assertThrows(WalletBalanceUpdateException.class, actual);
        verify(walletRepository, times(1))
                .withdraw(anyLong(), any());
    }

    @Test
    void givenWalletIdAndAmount_whenDeposit_thenThrowWalletBalanceUpdateException() {
        // given - precondition or setup
        var amount  = BigDecimal.ONE;
        var expectedWalletFirst = new Wallet();
        expectedWalletFirst.setId(1L);

        // when - action or the behaviour that we are going test
        Executable actual = () -> walletService.deposit(expectedWalletFirst.getId(), amount);

        // then - verify the output
        assertThrows(WalletBalanceUpdateException.class, actual);
        verify(walletRepository, times(1))
                .deposit(anyLong(), any());
    }
}