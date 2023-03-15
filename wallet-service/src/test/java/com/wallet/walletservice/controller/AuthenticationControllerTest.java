package com.wallet.walletservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wallet.core.model.User;
import com.wallet.core.rest.Response;
import com.wallet.walletservice.repository.UserRepository;
import com.wallet.walletservice.service.AuthenticationService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthenticationController.class)
class AuthenticationControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Mock
    UserRepository userRepository;

    @MockBean
    AuthenticationService authenticationService;

    @Test
    void givenUserEmailAndPassword_whenLogin_thenReturnSuccess() throws Exception {
        // given - precondition or setup
        var user = new User();
        user.setId(1L);
        user.setEmail("foo@bar.com");
        user.setPassword("foobar");
        var token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJ1c2VySWQiOjF9.JYAaZ6_q5z2I9lNbAdgpmTkUvXkrtAB7mqBb6oLBxAszigwXQld_dFJRU3aAfF2mQZtu1I5TpsvtpElctGN4Yg";

        // when - action or the behaviour that we are going test
        given(authenticationService.login(anyString(), anyString())).willReturn(token);
        given(userRepository.findByEmail(anyString())).willReturn(Optional.of(user));

        ResponseEntity<Response<String>> expectedResult = ResponseEntity.ok(Response.success(token));
        String responseBodyJson = objectMapper.writeValueAsString(expectedResult.getBody());

        // then - verify the output
        mockMvc.perform(get(UriComponentsBuilder.fromUriString("/api/authentication/login")
                        .build().toUri())
                        .contentType(MediaType.APPLICATION_JSON)
                        .queryParam("email", user.getEmail())
                        .queryParam("password", user.getPassword()))
                .andDo(print())
                .andExpect(status().is(HttpStatus.OK.value()))
                .andExpect(content().json(responseBodyJson));
    }

    @Test
    void givenToken_whenVerify_thenReturnSuccess() throws Exception {
        // given - precondition or setup
        var token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJ1c2VySWQiOjF9.JYAaZ6_q5z2I9lNbAdgpmTkUvXkrtAB7mqBb6oLBxAszigwXQld_dFJRU3aAfF2mQZtu1I5TpsvtpElctGN4Yg";

        // then - verify the output
        mockMvc.perform(get(UriComponentsBuilder.fromUriString("/api/authentication/verify")
                        .build().toUri())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token))
                .andDo(print())
                .andExpect(status().is(HttpStatus.OK.value()));
    }
}