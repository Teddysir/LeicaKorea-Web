package com.example.leica_refactoring.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public MemberLoginResponseDto login(MemberLoginRequestDto requestDto, HttpServletResponse response) {
        this.setJwtTokenInHeader(requestDto.getMemberId(), response);

        return MemberLoginResponseDto.builder()
                .responseCode("200")
                .build();
    }

    private void setJwtTokenInHeader(String memberId, HttpServletResponse response) {
        UserRole userRole = memberRepository.findByMemberId(memberId).getUserRole();

        String accessToken = jwtTokenProvider.createAccessToken(memberId, userRole);
        String refreshToken = jwtTokenProvider.createRefreshToken(memberId, userRole);

        jwtTokenProvider.setHeaderAccessToken(response, accessToken);
        jwtTokenProvider.setHeaderRefreshToken(response, refreshToken);


    }
}
