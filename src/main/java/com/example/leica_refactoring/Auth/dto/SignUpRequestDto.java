package com.example.leica_refactoring.Auth.dto;

import com.example.leica_refactoring.Auth.annotation.Authorities;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
public class SignUpRequestDto {
    @NotBlank
    private String memberId;

    @NotBlank
    private String password;

    @NotBlank
    private String username;

    @Authorities
    private String[] authorities;

    @Builder
    public SignUpRequestDto(String memberId, String password, String username, String[] authorities) {
        this.memberId = memberId;
        this.password = password;
        this.username = username;
        this.authorities = authorities;
    }
}