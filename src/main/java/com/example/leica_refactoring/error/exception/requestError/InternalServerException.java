package com.example.leica_refactoring.error.exception.requestError;

import com.example.leica_refactoring.error.security.ErrorCode;

public class InternalServerException extends BusinessException{

    public InternalServerException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }
}
