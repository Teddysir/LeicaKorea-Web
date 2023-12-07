package com.example.leica_refactoring.error.exception;

public class ParentCategoryMaxException extends RuntimeException {
    public ParentCategoryMaxException(Long maxParentCategoryCount) {
        super("메인 카테고리의 갯수는 " + maxParentCategoryCount + "개가 넘을 수 없습니다.");
    }
}
