package com.example.leica_refactoring.error.exception;

public class CategoryIsNotExists extends RuntimeException {
    public CategoryIsNotExists(String name) {
        super("존재하지 않는 카테고리 이름입니다." + name);
    }
}
