package com.example.leica_refactoring.error.exception;

public class ParentCategoryNotFoundException extends RuntimeException {
    public ParentCategoryNotFoundException(String parentName) {
        super("존재하지 않는 카테고리입니다." + parentName);
    }
}
