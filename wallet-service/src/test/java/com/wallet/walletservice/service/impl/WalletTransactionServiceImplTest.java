package com.wallet.walletservice.service.impl;

import com.wallet.core.enums.TransactionStatus;
import com.wallet.core.enums.TransactionType;
import com.wallet.core.model.Wallet;
import com.wallet.core.model.WalletTransaction;
import com.wallet.walletservice.exception.InsufficientBalanceException;
import com.wallet.walletservice.exception.TransactionRequestNotValidException;
import com.wallet.walletservice.exception.WalletNotFoundException;
import com.wallet.walletservice.repository.WalletTransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static com.wallet.core.enums.TransactionType.DEPOSIT;
import static com.wallet.core.enums.TransactionType.TRANSFER;
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
    void givenWalletTransactionDepositWithoutReceiverWallet_whenCreate_thenThrowTransactionRequestNotValidException() {
        // given - precondition or setup
        var expectedWalletTransaction = new WalletTransaction();
        expectedWalletTransaction.setId(1L);
        expectedWalletTransaction.setTransactionType(DEPOSIT);

        // when - action or the behaviour that we are going test
        Executable actual = () -> walletTransactionService.create(expectedWalletTransaction);

        // then - verify the output
        assertThrows(TransactionRequestNotValidException.class, actual);
        verify(walletTransactionRepository, times(0)).save(any());
    }

    @Test
    void givenWalletTransactionDepositWithoutAmount_whenCreate_thenThrowTransactionRequestNotValidException() {
        // given - precondition or setup
        var expectedWalletTransaction = new WalletTransaction();
        expectedWalletTransaction.setId(1L);
        expectedWalletTransaction.setTransactionType(DEPOSIT);
        expectedWalletTransaction.setReceiverWallet(receiverWallet);

        // when - action or the behaviour that we are going test
        Executable actual = () -> walletTransactionService.create(expectedWalletTransaction);

        // then - verify the output
        assertThrows(TransactionRequestNotValidException.class, actual);
        verify(walletTransactionRepository, times(0)).save(any());
    }

    @Test
    void givenWalletTransactionDepositWitAmountIsEqualAndLessThanZero_whenCreate_thenThrowTransactionRequestNotValidException() {
        // given - precondition or setup
        var expectedWalletTransaction = new WalletTransaction();
        expectedWalletTransaction.setId(1L);
        expectedWalletTransaction.setTransactionType(DEPOSIT);
        expectedWalletTransaction.setReceiverWallet(receiverWallet);
        expectedWalletTransaction.setAmount(BigDecimal.ZERO);

        // when - action or the behaviour that we are going test
        Executable actual = () -> walletTransactionService.create(expectedWalletTransaction);

        // then - verify the output
        assertThrows(TransactionRequestNotValidException.class, actual);
        verify(walletTransactionRepository, times(0)).save(any());
    }

    @Test
    void givenWalletTransactionDeposit_whenCreate_thenThrowWalletNotFoundException() {
        // given - precondition or setup
        var expectedWalletTransaction = new WalletTransaction();
        expectedWalletTransaction.setReceiverWallet(receiverWallet);
        expectedWalletTransaction.setAmount(BigDecimal.ONE);
        expectedWalletTransaction.setTransactionType(TransactionType.DEPOSIT);

        // when - action or the behaviour that we are going test
        Executable actual = () -> walletTransactionService.create(expectedWalletTransaction);

        // then - verify the output
        assertThrows(WalletNotFoundException.class, actual);
        verify(walletTransactionRepository, times(0)).save(any());
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

    @Test
    void givenWalletTransactionWithdrawWitAmountIsEqualAndLessThanZero_whenCreate_thenThrowTransactionRequestNotValidException() {
        // given - precondition or setup
        var expectedWalletTransaction = new WalletTransaction();
        expectedWalletTransaction.setId(1L);
        expectedWalletTransaction.setTransactionType(WITHDRAW);
        expectedWalletTransaction.setSenderWallet(senderWallet);
        expectedWalletTransaction.setAmount(BigDecimal.ZERO);

        // when - action or the behaviour that we are going test
        Executable actual = () -> walletTransactionService.create(expectedWalletTransaction);

        // then - verify the output
        assertThrows(TransactionRequestNotValidException.class, actual);
        verify(walletTransactionRepository, times(0)).save(any());
    }

    @Test
    void givenWalletTransactionWithdraw_whenCreate_thenThrowWalletNotFoundException() {
        // given - precondition or setup
        var expectedWalletTransaction = new WalletTransaction();
        expectedWalletTransaction.setSenderWallet(senderWallet);
        expectedWalletTransaction.setAmount(BigDecimal.ONE);
        expectedWalletTransaction.setTransactionType(WITHDRAW);

        // when - action or the behaviour that we are going test
        Executable actual = () -> walletTransactionService.create(expectedWalletTransaction);

        // then - verify the output
        assertThrows(WalletNotFoundException.class, actual);
        verify(walletTransactionRepository, times(0)).save(any());
    }

    @Test
    void givenWalletTransactionTransfer_whenCreate_thenReturnSuccess() {
        // given - precondition or setup
        var expectedWalletTransaction = new WalletTransaction();
        expectedWalletTransaction.setId(1L);
        expectedWalletTransaction.setTransactionId(UUID.randomUUID());
        senderWallet.setBalance(BigDecimal.ONE);
        expectedWalletTransaction.setSenderWallet(senderWallet);
        expectedWalletTransaction.setReceiverWallet(receiverWallet);
        expectedWalletTransaction.setTransactionStatus(TransactionStatus.COMPLETED);
        expectedWalletTransaction.setAmount(BigDecimal.ONE);
        expectedWalletTransaction.setDescription("Transfer Example");
        expectedWalletTransaction.setCreatedAt(new Date());
        expectedWalletTransaction.setTransactionType(TransactionType.TRANSFER);
        given(walletTransactionRepository.save(any())).willReturn(expectedWalletTransaction);
        given(walletService.find(senderWallet.getId())).willReturn(senderWallet);
        given(walletService.find(receiverWallet.getId())).willReturn(receiverWallet);

        // when - action or the behaviour that we are going test
        var actualWalletTransaction = walletTransactionService.create(expectedWalletTransaction);

        // then - verify the output
        assertNotNull(actualWalletTransaction);
        assertNotNull(actualWalletTransaction.getId());
        assertNotNull(actualWalletTransaction.getReceiverWallet());
        assertNotNull(actualWalletTransaction.getSenderWallet());
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
    void givenWalletTransactionTransfer_whenCreate_thenThrowInsufficientBalanceException() {
        // given - precondition or setup
        var expectedWalletTransaction = new WalletTransaction();
        expectedWalletTransaction.setId(1L);
        expectedWalletTransaction.setTransactionId(UUID.randomUUID());
        expectedWalletTransaction.setSenderWallet(senderWallet);
        expectedWalletTransaction.setReceiverWallet(receiverWallet);
        expectedWalletTransaction.setTransactionStatus(TransactionStatus.COMPLETED);
        expectedWalletTransaction.setAmount(BigDecimal.ONE);
        expectedWalletTransaction.setDescription("Transfer Example");
        expectedWalletTransaction.setCreatedAt(new Date());
        expectedWalletTransaction.setTransactionType(TRANSFER);
        given(walletService.find(senderWallet.getId())).willReturn(senderWallet);
        given(walletService.find(receiverWallet.getId())).willReturn(receiverWallet);

        // when - action or the behaviour that we are going test
        Executable actual = () -> walletTransactionService.create(expectedWalletTransaction);

        // then - verify the output
        assertThrows(InsufficientBalanceException.class, actual);
        verify(walletTransactionRepository, times(0)).save(any());
    }

    @Test
    void givenWalletTransactionTransferWithoutSenderAndReceiverWallet_whenCreate_thenThrowTransactionRequestNotValidException() {
        // given - precondition or setup
        var expectedWalletTransaction = new WalletTransaction();
        expectedWalletTransaction.setId(1L);
        expectedWalletTransaction.setTransactionType(TRANSFER);

        // when - action or the behaviour that we are going test
        Executable actual = () -> walletTransactionService.create(expectedWalletTransaction);

        // then - verify the output
        assertThrows(TransactionRequestNotValidException.class, actual);
        verify(walletTransactionRepository, times(0)).save(any());
    }

    @Test
    void givenWalletTransactionTransferWithoutReceiverWallet_whenCreate_thenThrowTransactionRequestNotValidException() {
        // given - precondition or setup
        var expectedWalletTransaction = new WalletTransaction();
        expectedWalletTransaction.setId(1L);
        expectedWalletTransaction.setTransactionType(TRANSFER);
        expectedWalletTransaction.setSenderWallet(senderWallet);

        // when - action or the behaviour that we are going test
        Executable actual = () -> walletTransactionService.create(expectedWalletTransaction);

        // then - verify the output
        assertThrows(TransactionRequestNotValidException.class, actual);
        verify(walletTransactionRepository, times(0)).save(any());
    }

    @Test
    void givenWalletTransactionTransferWithoutSenderWallet_whenCreate_thenThrowTransactionRequestNotValidException() {
        // given - precondition or setup
        var expectedWalletTransaction = new WalletTransaction();
        expectedWalletTransaction.setId(1L);
        expectedWalletTransaction.setTransactionType(TRANSFER);
        expectedWalletTransaction.setReceiverWallet(receiverWallet);

        // when - action or the behaviour that we are going test
        Executable actual = () -> walletTransactionService.create(expectedWalletTransaction);

        // then - verify the output
        assertThrows(TransactionRequestNotValidException.class, actual);
        verify(walletTransactionRepository, times(0)).save(any());
    }

    @Test
    void givenWalletTransactionTransferWithoutAmount_whenCreate_thenThrowTransactionRequestNotValidException() {
        // given - precondition or setup
        var expectedWalletTransaction = new WalletTransaction();
        expectedWalletTransaction.setId(1L);
        expectedWalletTransaction.setTransactionType(WITHDRAW);
        expectedWalletTransaction.setSenderWallet(senderWallet);
        expectedWalletTransaction.setReceiverWallet(receiverWallet);

        // when - action or the behaviour that we are going test
        Executable actual = () -> walletTransactionService.create(expectedWalletTransaction);

        // then - verify the output
        assertThrows(TransactionRequestNotValidException.class, actual);
        verify(walletTransactionRepository, times(0)).save(any());
    }

    @Test
    void givenWalletTransactionTransferWitAmountIsEqualAndLessThanZero_whenCreate_thenThrowTransactionRequestNotValidException() {
        // given - precondition or setup
        var expectedWalletTransaction = new WalletTransaction();
        expectedWalletTransaction.setId(1L);
        expectedWalletTransaction.setTransactionType(TRANSFER);
        expectedWalletTransaction.setSenderWallet(senderWallet);
        expectedWalletTransaction.setReceiverWallet(receiverWallet);
        expectedWalletTransaction.setAmount(BigDecimal.ZERO);

        // when - action or the behaviour that we are going test
        Executable actual = () -> walletTransactionService.create(expectedWalletTransaction);

        // then - verify the output
        assertThrows(TransactionRequestNotValidException.class, actual);
        verify(walletTransactionRepository, times(0)).save(any());
    }

    @Test
    void givenWalletTransactionTransfer_whenCreate_thenThrowWalletNotFoundException() {
        // given - precondition or setup
        var expectedWalletTransaction = new WalletTransaction();
        expectedWalletTransaction.setSenderWallet(senderWallet);
        expectedWalletTransaction.setReceiverWallet(receiverWallet);
        expectedWalletTransaction.setAmount(BigDecimal.ONE);
        expectedWalletTransaction.setTransactionType(TRANSFER);
        given(walletService.find(senderWallet.getId())).willReturn(senderWallet);

        // when - action or the behaviour that we are going test
        Executable actual = () -> walletTransactionService.create(expectedWalletTransaction);

        // then - verify the output
        assertThrows(WalletNotFoundException.class, actual);
        verify(walletTransactionRepository, times(0)).save(any());
    }

    @Test
    void givenWalletTransactionIsNull_whenCreate_thenThrowTransactionRequestNotValidException() {
        // when - action or the behaviour that we are going test
        Executable actual = () -> walletTransactionService.create(null);

        // then - verify the output
        assertThrows(TransactionRequestNotValidException.class, actual);
        verify(walletTransactionRepository, times(0)).save(any());
    }

    @Test
    void givenWalletTransactionWithTransactionTypeIsNull_whenCreate_thenThrowTransactionRequestNotValidException() {
        // given - precondition or setup
        var expectedWalletTransaction = new WalletTransaction();
        expectedWalletTransaction.setTransactionType(null);

        // when - action or the behaviour that we are going test
        Executable actual = () -> walletTransactionService.create(expectedWalletTransaction);

        // then - verify the output
        assertThrows(TransactionRequestNotValidException.class, actual);
        verify(walletTransactionRepository, times(0)).save(any());
    }

    @Test
    void givenWalletId_whenFindAllDebit_thenReturnSuccess() {
        // given - precondition or setup
        var expectedWalletTransactionFirst = new WalletTransaction();
        expectedWalletTransactionFirst.setId(1L);
        var expectedWalletTransactionSecond = new WalletTransaction();
        expectedWalletTransactionSecond.setId(2L);
        var expectedWalletTransaction =
                List.of(expectedWalletTransactionFirst, expectedWalletTransactionSecond);

        given(walletTransactionRepository.findAllBySenderWalletId(anyLong())).willReturn(expectedWalletTransaction);

        // when - action or the behaviour that we are going test
        var actualWalletTransaction = walletTransactionService.findAllDebitByWalletId(anyLong());

        // then - verify the output
        assertNotNull(actualWalletTransaction);
        verify(walletTransactionRepository).findAllBySenderWalletId(anyLong());
    }

    @Test
    void givenWalletId_whenFindAllCredit_thenReturnSuccess() {
        // given - precondition or setup
        var expectedWalletTransactionFirst = new WalletTransaction();
        expectedWalletTransactionFirst.setId(1L);
        var expectedWalletTransactionSecond = new WalletTransaction();
        expectedWalletTransactionSecond.setId(2L);
        var expectedWalletTransaction =
                List.of(expectedWalletTransactionFirst, expectedWalletTransactionSecond);

        given(walletTransactionRepository.findAllByReceiverWalletId(anyLong())).willReturn(expectedWalletTransaction);

        // when - action or the behaviour that we are going test
        var actualWalletTransaction = walletTransactionService.findAllCreditByWalletId(anyLong());

        // then - verify the output
        assertNotNull(actualWalletTransaction);
        verify(walletTransactionRepository).findAllByReceiverWalletId(anyLong());
    }
}