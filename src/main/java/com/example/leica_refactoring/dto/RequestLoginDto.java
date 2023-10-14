package com.example.leica_refactoring.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RequestLoginDto {
    private String memberId;
    private String password;

    public RequestLoginDto(String memberId, String password){
        this.memberId = memberId;
        this.password = password;
    }
}
