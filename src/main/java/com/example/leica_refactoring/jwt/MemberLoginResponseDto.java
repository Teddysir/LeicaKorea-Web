package com.example.leica_refactoring.jwt;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberLoginResponseDto {

    private String responseCode;

}
