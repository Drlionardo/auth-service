package com.example.authserver.auth.authentication;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

public class LoginAuthentication extends UsernamePasswordAuthenticationToken {

    public LoginAuthentication(Object principal, Object credentials) {
        super(principal, credentials);
    }
}
