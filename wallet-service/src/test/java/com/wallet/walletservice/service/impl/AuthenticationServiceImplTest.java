package com.wallet.walletservice.service.impl;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.wallet.core.exception.UnauthorizedException;
import com.wallet.core.model.User;
import com.wallet.walletservice.repository.UserRepository;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceImplTest {
    @Mock
    UserRepository userRepository;

    @Test
    void givenUserEmailAndPassword_whenLogin_ThenReturnSuccessful() {
        // given - precondition or setup
        var user = new User();
        user.setEmail("foo@bar.com");
        user.setPassword("foobar");

        // when - action or the behaviour that we are going test
        var authenticationService = new AuthenticationServiceImpl(userRepository);
        given(userRepository.findByEmail(anyString())).willReturn(Optional.ofNullable(user));
        var response = authenticationService.login(user.getEmail(), user.getPassword());

        // then - verify the output
        assertFalse(response.isEmpty());
    }

    @Test
    void givenUserIsNull_whenLogin_ThenThrowUnauthorizedException() {
        // given - precondition or setup
        var user = new User();
        user.setEmail("foo@bar.com");
        user.setPassword("foobar");

        // when - action or the behaviour that we are going test
        var authenticationService = new AuthenticationServiceImpl(userRepository);
        Executable actual = () -> authenticationService.login(user.getEmail(), user.getPassword());

        // then - verify the output
        assertThrows(UnauthorizedException.class, actual);
    }

    @Test
    void givenToken_whenVerify_thenReturnSuccessful() {
        // given - precondition or setup
        var token = "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJ1c2VySWQiOjF9.JYAaZ6_q5z2I9lNbAdgpmTkUvXkrtAB7mqBb6oLBxAszigwXQld_dFJRU3aAfF2mQZtu1I5TpsvtpElctGN4Yg";

        // when - action or the behaviour that we are going test
        var authenticationService = new AuthenticationServiceImpl(userRepository);

        // then - verify the output
        authenticationService.verify(token);
    }

    @Test
    void givenTokenIsNotValid_whenVerify_thenThrowJWTVerificationException() {
        // given - precondition or setup
        var token = StringUtils.EMPTY;

        // when - action or the behaviour that we are going test
        var authenticationService = new AuthenticationServiceImpl(userRepository);
        Executable actual = () -> authenticationService.verify(token);

        // then - verify the output
        assertThrows(JWTVerificationException.class, actual);
    }
}