package com.example.leica_refactoring.Auth.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class LoginFailException extends RuntimeException {

    public LoginFailException(String message) {
        super(message);
    }
}