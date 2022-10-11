package com.example.authserver.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class BadTokenFormatException extends ResponseStatusException {
    public BadTokenFormatException(String token) {
        super(HttpStatus.BAD_REQUEST, String.format("Bad token format. Token %s", token));
    }

}
