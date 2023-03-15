package com.wallet.walletservice.controller;

import com.wallet.core.rest.Response;
import com.wallet.walletservice.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/authentication")
@CrossOrigin(origins = "http://localhost:3000")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @GetMapping("/login")
    ResponseEntity<Response<String>> login(@RequestParam("email") String email,
                                           @RequestParam("password") String password) {
        return ResponseEntity.ok(
                Response.success(authenticationService.login(email, password)));
    }

    @GetMapping("/verify")
    public ResponseEntity<Void> verify(@RequestHeader("Authorization") String token) {
        authenticationService.verify(token);
        return ResponseEntity.ok().build();
    }
}
