package com.example.authserver.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpStatusCodeException;

public class UserAlreadyExistsException extends HttpStatusCodeException {

    public UserAlreadyExistsException(String email) {
        super(HttpStatus.BAD_REQUEST, String.format("User with email %s already exists", email));
    }
}
