package com.example.leica_refactoring.jwt;

import lombok.*;

@Builder
@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class MemberLoginRequestDto {

    private String memberId;

    private String password;

}
