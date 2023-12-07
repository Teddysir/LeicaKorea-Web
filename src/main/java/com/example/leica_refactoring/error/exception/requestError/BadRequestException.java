package com.example.leica_refactoring.error.exception.requestError;

import com.example.leica_refactoring.error.security.ErrorCode;

public class BadRequestException extends BusinessException{

    public BadRequestException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }
}
