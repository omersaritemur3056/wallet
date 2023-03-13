package com.wallet.walletservice.service;

import com.wallet.core.exception.EntityNotFoundException;
import com.wallet.core.model.Wallet;
import com.wallet.walletservice.exception.WalletDeleteException;
import com.wallet.walletservice.helper.WalletNameGenerator;
import com.wallet.walletservice.repository.WalletRepository;
import com.wallet.walletservice.service.impl.WalletServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
        expectedWallet.setBalance(BigDecimal.valueOf(100));
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
    void givenWalletId_whenDelete_thenReturnSuccess() {
        // given - precondition or setup
        var expectedWallet = new Wallet();
        expectedWallet.setId(1L);
        expectedWallet.setName(WalletNameGenerator.generate());
        expectedWallet.setCreatedAt(new Date());
        expectedWallet.setBalance(BigDecimal.ZERO);
        given(walletRepository.findById(anyLong())).willReturn(Optional.of(expectedWallet));

        // when - action or the behaviour that we are going test
        walletService.delete(expectedWallet.getId());

        // then - verify the output
        assertTrue(expectedWallet.isDeleted());
        verify(walletRepository).save(any());
    }

    @Test
    void givenWalletId_whenDelete_thenThrowWalletEntityNotFoundException() {
        given(walletRepository.findById(anyLong())).willReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> walletService.delete(anyLong()));
        verify(walletRepository, times(0)).deleteById(anyLong());
    }

    @Test
    void givenWalletId_whenDeleteBalanceIsPositive_thenThrowWalletEntityNotFoundException() {
        var expectedWallet = new Wallet();
        expectedWallet.setId(1L);
        expectedWallet.setName(WalletNameGenerator.generate());
        expectedWallet.setCreatedAt(new Date());
        expectedWallet.setBalance(BigDecimal.ONE);

        given(walletRepository.findById(anyLong())).willReturn(Optional.of(expectedWallet));
        assertThrows(WalletDeleteException.class, () -> walletService.delete(anyLong()));
        verify(walletRepository, times(0)).deleteById(anyLong());
    }


}