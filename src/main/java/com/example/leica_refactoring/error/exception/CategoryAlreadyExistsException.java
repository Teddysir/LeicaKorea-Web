package com.example.leica_refactoring.error.exception;

public class CategoryAlreadyExistsException extends RuntimeException {
    public CategoryAlreadyExistsException(String name) {
        super("이미 존재하는 카테고리 입니다." + name);
    }
}
