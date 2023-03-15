package com.wallet.walletservice.controller;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wallet.core.enums.TransactionStatus;
import com.wallet.core.model.Wallet;
import com.wallet.core.model.WalletTransaction;
import com.wallet.core.rest.Response;
import com.wallet.walletservice.service.WalletTransactionService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static com.wallet.core.enums.TransactionStatus.COMPLETED;
import static com.wallet.core.enums.TransactionType.DEPOSIT;
import static com.wallet.core.enums.TransactionType.TRANSFER;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(WalletTransactionController.class)
class WalletTransactionControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    WalletTransactionService walletTransactionService;

    private Wallet receiverWallet;
    private Wallet senderWallet;

    @BeforeEach
    void setup() {
        receiverWallet = new Wallet();
        receiverWallet.setId(1L);
        senderWallet = new Wallet();
        senderWallet.setId(2L);
    }

    @Test
    void givenWalletTransaction_whenCreate_thenReturnSuccess() throws Exception {
        // given - precondition or setup
        var walletTransaction = new WalletTransaction();
        walletTransaction.setId(1L);
        walletTransaction.setReceiverWallet(receiverWallet);
        walletTransaction.setAmount(BigDecimal.ONE);
        walletTransaction.setTransactionType(DEPOSIT);
        walletTransaction.setSenderWallet(null);
        walletTransaction.setTransactionStatus(COMPLETED);
        walletTransaction.setDescription("Deposit Example");

        // when - action or the behaviour that we are going test
        Mockito.when(walletTransactionService.create(Mockito.any())).thenReturn(walletTransaction);
        String walletTransactionJson = objectMapper.writeValueAsString(walletTransaction);
        ResponseEntity<Response<WalletTransaction>> expectedResult = ResponseEntity.ok(
                Response.success(walletTransaction));
        String responseBodyJson = objectMapper.writeValueAsString(expectedResult.getBody());

        // then - verify the output
        mockMvc.perform(post(UriComponentsBuilder.fromUriString("/api/transactions")
                        .build().toUri())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(walletTransactionJson))
                .andDo(print())
                .andExpect(status().is(HttpStatus.OK.value()))
                .andExpect(content().json(responseBodyJson));
    }

    @Test
    void givenWalletId_whenFindAllByWalletId_thenReturnSuccess() throws Exception {
        // given - precondition or setup
        var walletTransaction = new WalletTransaction();
        walletTransaction.setId(1L);
        walletTransaction.setReceiverWallet(receiverWallet);
        walletTransaction.setAmount(BigDecimal.ONE);
        walletTransaction.setTransactionType(DEPOSIT);
        walletTransaction.setSenderWallet(null);
        walletTransaction.setTransactionStatus(COMPLETED);
        walletTransaction.setDescription("Deposit Example");
        var walletTransactions = List.of(walletTransaction);

        // when - action or the behaviour that we are going test
        Mockito.when(walletTransactionService.findAllDebitByWalletId(Mockito.any())).thenReturn(walletTransactions);
        String walletTransactionJson = objectMapper.writeValueAsString(List.of(walletTransactions));
        ResponseEntity<Response<List<WalletTransaction>>> expectedResult = ResponseEntity.ok(
                Response.success(walletTransactions));
        String responseBodyJson = objectMapper.writeValueAsString(expectedResult.getBody());

        // then - verify the output
        mockMvc.perform(get(UriComponentsBuilder.fromUriString("/api/transactions/" + receiverWallet.getId())
                        .build().toUri())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(walletTransactionJson))
                .andDo(print())
                .andExpect(status().is(HttpStatus.OK.value()))
                .andExpect(content().json(responseBodyJson));
    }

}
