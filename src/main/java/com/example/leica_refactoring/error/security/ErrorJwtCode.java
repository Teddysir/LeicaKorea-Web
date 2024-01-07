package com.example.leica_refactoring.error.security;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public enum ErrorJwtCode {

    INVALID_JWT_FORMAT("1001","1001 INVALID JWT FORMAT"),
    UNSUPPORTED_JWT_TOKEN("1003","1003 UNSUPPORTED JWT TOKEN"),
    INVALID_VALUE("1004","1004 INVALID VALUE"),
    RUNTIME_EXCEPTION("1005","1005 RUNTIME EXCEPTION"),
    EXPIRED_REFRESH_TOKEN("1006","1006 EXPIRED REFRESH TOKEN"),
    EXPIRED_ACCESS_TOKEN("1007","1007 EXPIRED ACCESS TOKEN");

    private final String code;
    private final String message;

    ErrorJwtCode(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
