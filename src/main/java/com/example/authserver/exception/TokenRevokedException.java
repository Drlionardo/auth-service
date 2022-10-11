package com.example.authserver.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class TokenRevokedException extends ResponseStatusException {

    public TokenRevokedException(String token) {
        super(HttpStatus.FORBIDDEN, String.format("Token %s was revoked",
                token.substring(0, Math.min(token.length(), 8)) + "..."));
    }
}
