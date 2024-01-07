package com.example.leica_refactoring.error.exception.requestError;

import com.example.leica_refactoring.error.security.ErrorCode;

public class ExpiredAccessTokenException extends BusinessException {

    public ExpiredAccessTokenException(String message, ErrorCode code) {
        super(message, code);
    }
}

