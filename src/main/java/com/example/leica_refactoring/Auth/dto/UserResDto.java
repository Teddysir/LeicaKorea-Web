package com.example.leica_refactoring.Auth.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UserResDto {
    private String memberId;
    private String username;

    @Builder
    public UserResDto(String memberId, String username) {
        this.memberId = memberId;
        this.username = username;
    }
}