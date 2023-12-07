package com.example.leica_refactoring.error.exception;

public class AuthorOnlyAccessException extends RuntimeException {
    public AuthorOnlyAccessException() {
        super("작성자만 수정할 수 있습니다.");
    }
}
