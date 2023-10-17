package com.example.leica_refactoring.Auth.annotation;

public enum AuthorityName {
    ROLE_USER("ROLE_USER"),
    ROLE_ADMIN("ROLE_ADMIN");

    String value;

    AuthorityName(String value) {
        this.value = value;
    }
}