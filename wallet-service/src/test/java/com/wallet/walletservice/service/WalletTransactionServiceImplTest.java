package com.wallet.walletservice.service;

import com.wallet.core.enums.TransactionStatus;
import com.wallet.core.enums.TransactionType;
import com.wallet.core.exception.EntityNotFoundException;
import com.wallet.core.model.Wallet;
import com.wallet.core.model.WalletTransaction;
import com.wallet.walletservice.exception.InsufficientBalanceException;
import com.wallet.walletservice.exception.TransactionRequestNotValidException;
import com.wallet.walletservice.helper.WalletNameGenerator;
import com.wallet.walletservice.repository.WalletTransactionRepository;
import com.wallet.walletservice.service.impl.WalletServiceImpl;
import com.wallet.walletservice.service.impl.WalletTransactionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import static com.wallet.core.enums.TransactionType.WITHDRAW;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class WalletTransactionServiceImplTest {
    @InjectMocks
    private WalletTransactionServiceImpl walletTransactionService;
    @Mock
    private WalletServiceImpl walletService;
    @Mock
    private WalletTransactionRepository walletTransactionRepository;

    private Wallet receiverWallet;
    private Wallet senderWallet;

    @BeforeEach
    public void setup() {
        receiverWallet = new Wallet();
        receiverWallet.setId(1L);
        senderWallet = new Wallet();
        senderWallet.setId(2L);
    }

    @Test
    void givenWalletTransactionDeposit_whenCreate_thenReturnSuccess() {
        // given - precondition or setup
        var expectedWalletTransaction = new WalletTransaction();
        expectedWalletTransaction.setId(1L);
        expectedWalletTransaction.setTransactionId(UUID.randomUUID());
        expectedWalletTransaction.setReceiverWallet(receiverWallet);
        expectedWalletTransaction.setTransactionStatus(TransactionStatus.COMPLETED);
        expectedWalletTransaction.setAmount(BigDecimal.ONE);
        expectedWalletTransaction.setDescription("Deposit Example");
        expectedWalletTransaction.setCreatedAt(new Date());
        expectedWalletTransaction.setTransactionType(TransactionType.DEPOSIT);
        given(walletTransactionRepository.save(any())).willReturn(expectedWalletTransaction);
        given(walletService.find(receiverWallet.getId())).willReturn(receiverWallet);

        // when - action or the behaviour that we are going test
        var actualWalletTransaction = walletTransactionService.create(expectedWalletTransaction);

        // then - verify the output
        assertNotNull(actualWalletTransaction);
        assertNotNull(actualWalletTransaction.getId());
        assertNotNull(actualWalletTransaction.getReceiverWallet());
        assertNull(actualWalletTransaction.getSenderWallet());
        assertEquals(expectedWalletTransaction.getId(), actualWalletTransaction.getId());
        assertEquals(expectedWalletTransaction.getTransactionId(), actualWalletTransaction.getTransactionId());
        assertEquals(expectedWalletTransaction.getCreatedAt(), actualWalletTransaction.getCreatedAt());
        assertEquals(expectedWalletTransaction.getAmount(), actualWalletTransaction.getAmount());
        assertEquals(expectedWalletTransaction.getReceiverWallet(), actualWalletTransaction.getReceiverWallet());
        assertEquals(expectedWalletTransaction.getTransactionType(), actualWalletTransaction.getTransactionType());
        assertEquals(expectedWalletTransaction.getTransactionStatus(), actualWalletTransaction.getTransactionStatus());
        assertEquals(expectedWalletTransaction.getDescription(), actualWalletTransaction.getDescription());
        verify(walletTransactionRepository).save(any());
    }

    @Test
    void givenWalletTransactionWithdraw_whenCreate_thenReturnSuccess() {
        // given - precondition or setup
        var expectedWalletTransaction = new WalletTransaction();
        expectedWalletTransaction.setId(1L);
        expectedWalletTransaction.setTransactionId(UUID.randomUUID());
        senderWallet.setBalance(BigDecimal.ONE);
        expectedWalletTransaction.setSenderWallet(senderWallet);
        expectedWalletTransaction.setTransactionStatus(TransactionStatus.COMPLETED);
        expectedWalletTransaction.setAmount(BigDecimal.ONE);
        expectedWalletTransaction.setDescription("Withdraw Example");
        expectedWalletTransaction.setCreatedAt(new Date());
        expectedWalletTransaction.setTransactionType(WITHDRAW);
        given(walletTransactionRepository.save(any())).willReturn(expectedWalletTransaction);
        given(walletService.find(senderWallet.getId())).willReturn(senderWallet);

        // when - action or the behaviour that we are going test
        var actualWalletTransaction = walletTransactionService.create(expectedWalletTransaction);

        // then - verify the output
        assertNotNull(actualWalletTransaction);
        assertNotNull(actualWalletTransaction.getId());
        assertNotNull(actualWalletTransaction.getSenderWallet());
        assertNull(actualWalletTransaction.getReceiverWallet());
        assertEquals(expectedWalletTransaction.getId(), actualWalletTransaction.getId());
        assertEquals(expectedWalletTransaction.getTransactionId(), actualWalletTransaction.getTransactionId());
        assertEquals(expectedWalletTransaction.getCreatedAt(), actualWalletTransaction.getCreatedAt());
        assertEquals(expectedWalletTransaction.getAmount(), actualWalletTransaction.getAmount());
        assertEquals(expectedWalletTransaction.getSenderWallet(), actualWalletTransaction.getSenderWallet());
        assertEquals(expectedWalletTransaction.getTransactionType(), actualWalletTransaction.getTransactionType());
        assertEquals(expectedWalletTransaction.getTransactionStatus(), actualWalletTransaction.getTransactionStatus());
        assertEquals(expectedWalletTransaction.getDescription(), actualWalletTransaction.getDescription());
        verify(walletTransactionRepository).save(any());
    }

    @Test
    void givenWalletTransactionWithdraw_whenCreate_thenThrowInsufficientBalanceException() {
        // given - precondition or setup
        var expectedWalletTransaction = new WalletTransaction();
        expectedWalletTransaction.setId(1L);
        expectedWalletTransaction.setTransactionId(UUID.randomUUID());
        expectedWalletTransaction.setSenderWallet(senderWallet);
        expectedWalletTransaction.setTransactionStatus(TransactionStatus.COMPLETED);
        expectedWalletTransaction.setAmount(BigDecimal.ONE);
        expectedWalletTransaction.setDescription("Withdraw Example");
        expectedWalletTransaction.setCreatedAt(new Date());
        expectedWalletTransaction.setTransactionType(WITHDRAW);
        given(walletService.find(senderWallet.getId())).willReturn(senderWallet);

        // when - action or the behaviour that we are going test
        Executable actual = () -> walletTransactionService.create(expectedWalletTransaction);

        // then - verify the output
        assertThrows(InsufficientBalanceException.class, actual);
        verify(walletTransactionRepository, times(0)).save(any());
    }

    @Test
    void givenWalletTransactionWithoutTransactionType_whenCreate_thenThrowTransactionRequestNotValidException() {
        // given - precondition or setup
        var expectedWalletTransaction = new WalletTransaction();
        expectedWalletTransaction.setId(1L);

        // when - action or the behaviour that we are going test
        Executable actual = () -> walletTransactionService.create(expectedWalletTransaction);

        // then - verify the output
        assertThrows(TransactionRequestNotValidException.class, actual);
        verify(walletTransactionRepository, times(0)).save(any());
    }

    @Test
    void givenWalletTransactionWithdrawWithoutSenderWallet_whenCreate_thenThrowTransactionRequestNotValidException() {
        // given - precondition or setup
        var expectedWalletTransaction = new WalletTransaction();
        expectedWalletTransaction.setId(1L);
        expectedWalletTransaction.setTransactionType(WITHDRAW);

        // when - action or the behaviour that we are going test
        Executable actual = () -> walletTransactionService.create(expectedWalletTransaction);

        // then - verify the output
        assertThrows(TransactionRequestNotValidException.class, actual);
        verify(walletTransactionRepository, times(0)).save(any());
    }

    @Test
    void givenWalletTransactionWithdrawWithoutAmount_whenCreate_thenThrowTransactionRequestNotValidException() {
        // given - precondition or setup
        var expectedWalletTransaction = new WalletTransaction();
        expectedWalletTransaction.setId(1L);
        expectedWalletTransaction.setTransactionType(WITHDRAW);
        expectedWalletTransaction.setSenderWallet(senderWallet);

        // when - action or the behaviour that we are going test
        Executable actual = () -> walletTransactionService.create(expectedWalletTransaction);

        // then - verify the output
        assertThrows(TransactionRequestNotValidException.class, actual);
        verify(walletTransactionRepository, times(0)).save(any());
    }
}