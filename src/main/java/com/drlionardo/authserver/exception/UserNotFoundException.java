package com.drlionardo.authserver.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpStatusCodeException;

public class UserNotFoundException extends HttpStatusCodeException {

    public UserNotFoundException(Long userId) {
        super(HttpStatus.NOT_FOUND, String.format("User with id %s not found", userId));
    }

    public UserNotFoundException(String email) {
        super(HttpStatus.NOT_FOUND, String.format("User with email %s not found", email));
    }
}
