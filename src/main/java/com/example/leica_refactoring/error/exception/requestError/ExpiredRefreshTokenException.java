package com.example.leica_refactoring.error.exception.requestError;

import com.example.leica_refactoring.error.security.ErrorCode;

public class ExpiredRefreshTokenException extends BusinessException {

    public ExpiredRefreshTokenException(String message, ErrorCode code) {
        super(message, code);
    }
}
