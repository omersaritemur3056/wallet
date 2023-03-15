package com.wallet.walletservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wallet.core.model.Wallet;
import com.wallet.core.rest.Response;
import com.wallet.walletservice.service.WalletService;
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

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(WalletController.class)
public class WalletControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    WalletService walletService;

    @Test
    void givenWallet_whenCreate_thenReturnSuccess() throws Exception {
        // given - precondition or setup
        var wallet = new Wallet();
        wallet.setId(1L);

        // when - action or the behaviour that we are going test
        Mockito.when(walletService.create(any())).thenReturn(wallet);
        String walletJson = objectMapper.writeValueAsString(wallet);
        ResponseEntity<Response<Wallet>> expectedResult = ResponseEntity.ok(
                Response.success(wallet));
        String responseBodyJson = objectMapper.writeValueAsString(expectedResult.getBody());

        // then - verify the output
        mockMvc.perform(post(UriComponentsBuilder.fromUriString("/api/wallets")
                        .build().toUri())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(walletJson))
                .andDo(print())
                .andExpect(status().is(HttpStatus.OK.value()))
                .andExpect(content().json(responseBodyJson));
    }

    @Test
    void givenUserId_whenFindAllByUserId_thenReturnSuccess() throws Exception {
        // given - precondition or setup
        var wallet = new Wallet();
        wallet.setId(1L);
        var wallets = List.of(wallet);

        // when - action or the behaviour that we are going test
        Mockito.when(walletService.findAllByUserId(anyLong())).thenReturn(wallets);
        String walletJson = objectMapper.writeValueAsString(List.of(wallets));
        ResponseEntity<Response<List<Wallet>>> expectedResult = ResponseEntity.ok(
                Response.success(wallets));
        String responseBodyJson = objectMapper.writeValueAsString(expectedResult.getBody());

        // then - verify the output
        mockMvc.perform(get(UriComponentsBuilder.fromUriString("/api/wallets/list/1")
                        .build().toUri())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(walletJson))
                .andDo(print())
                .andExpect(status().is(HttpStatus.OK.value()))
                .andExpect(content().json(responseBodyJson));
    }

    @Test
    void givenWalletId_whenDelete_thenReturnSuccess() throws Exception {
        // given - precondition or setup
        var wallet = new Wallet();
        wallet.setId(1L);

        // when - action or the behaviour that we are going test
        walletService.delete(anyLong());

        // then - verify the output
        mockMvc.perform(delete(UriComponentsBuilder.fromUriString("/api/wallets/" + wallet.getId())
                        .build().toUri()))
                .andDo(print())
                .andExpect(status().is(HttpStatus.RESET_CONTENT.value()));
    }

    @Test
    void givenWalletId_whenFind_thenReturnSuccess() throws Exception {
        // given - precondition or setup
        var wallet = new Wallet();
        wallet.setId(1L);

        // when - action or the behaviour that we are going test
        Mockito.when(walletService.find(anyLong())).thenReturn(wallet);
        String walletJson = objectMapper.writeValueAsString(wallet);
        ResponseEntity<Response<Wallet>> expectedResult = ResponseEntity.ok(
                Response.success(wallet));
        String responseBodyJson = objectMapper.writeValueAsString(expectedResult.getBody());

        // then - verify the output
        mockMvc.perform(get(UriComponentsBuilder.fromUriString("/api/wallets/" + wallet.getId())
                        .build().toUri())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(walletJson))
                .andDo(print())
                .andExpect(status().is(HttpStatus.OK.value()))
                .andExpect(content().json(responseBodyJson));
    }
}
