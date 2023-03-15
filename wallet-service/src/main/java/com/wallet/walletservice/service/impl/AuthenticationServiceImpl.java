package com.wallet.walletservice.service.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.wallet.core.exception.UnauthorizedException;
import com.wallet.walletservice.repository.UserRepository;
import com.wallet.walletservice.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@RequiredArgsConstructor
@Service
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository userRepository;
    private static final String SECRET = String.valueOf(javax.crypto.Cipher.SECRET_KEY);
    @Override
    public String login(String email, String password) {
        var optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isEmpty()) {
            throw new UnauthorizedException("User not found!");
        }

        var payload = new HashMap<String, Object>();
        payload.put("userId", optionalUser.get().getId());
        return JWT.create()
                .withPayload(payload)
                .sign(Algorithm.HMAC512(SECRET));
    }

    @Override
    public void verify(String token) {
        if(token != null && token.contains("Bearer ")) {
            JWT.require(Algorithm.HMAC512(SECRET)).build().verify(token.substring(7));
        } else {
            throw new JWTVerificationException("Token not valid");
        }
    }
}
