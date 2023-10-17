package com.example.leica_refactoring.Auth.dto;

import com.example.leica_refactoring.Auth.annotation.AuthorityName;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@RequiredArgsConstructor
public class UserWithAuthoritiesResDto {
    private String memberId;
    private String username;
    private List<AuthorityName> authorities;

    @Builder
    public UserWithAuthoritiesResDto(String memberId, String username, List<AuthorityName> authorities) {
        this.memberId = memberId;
        this.username = username;
        this.authorities = authorities;
    }
}