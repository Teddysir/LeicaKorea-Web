package com.example.leica_refactoring.jwt;

import com.example.leica_refactoring.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    public MemberLoginResponseDto login(MemberLoginRequestDto requestDto,HttpServletResponse response) {

        Member member = memberRepository.findByMemberId(requestDto.getMemberId());

        if (!passwordEncoder.matches(requestDto.getPassword(), member.getPassword())) {
            throw new IllegalArgumentException();
        }

        this.setJwtTokenInHeader(requestDto.getMemberId(),response);

        return MemberLoginResponseDto.builder()
                .responseCode("200")
                .build();
    }

    private void setJwtTokenInHeader(String memberId,HttpServletResponse response) {
        UserRole userRole = memberRepository.findByMemberId(memberId).getUserRole();

        String accessToken = jwtTokenProvider.createAccessToken(memberId, userRole);
        String refreshToken = jwtTokenProvider.createRefreshToken(memberId, userRole);

        jwtTokenProvider.setHeaderAccessToken(response, accessToken);
        jwtTokenProvider.setHeaderRefreshToken(response, refreshToken);

    }

    public Member findMemberByToken(HttpServletRequest request) {
        String token = jwtTokenProvider.resolveAccessToken(request);
        return token == null ? null : memberRepository.findByMemberId(jwtTokenProvider.getMemberId(token));
    }

}
