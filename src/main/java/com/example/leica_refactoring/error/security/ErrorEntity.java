package com.example.leica_refactoring.error.security;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

@Getter
@ToString
public class ErrorEntity {

    private String code;
    private String message;

    @Builder
    public ErrorEntity(String code, String message) {
        this.code = code;
        this.message = message;
    }
}