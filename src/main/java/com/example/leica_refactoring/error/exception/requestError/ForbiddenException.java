package com.example.leica_refactoring.error.exception.requestError;

import com.example.leica_refactoring.error.security.ErrorCode;

public class ForbiddenException extends BusinessException{

    public ForbiddenException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }
}

