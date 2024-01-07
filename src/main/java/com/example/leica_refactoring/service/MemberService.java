package com.example.leica_refactoring.service;

import com.example.leica_refactoring.dto.member.MemberLoginRequestDto;
import com.example.leica_refactoring.dto.member.MemberLoginResponseDto;
import com.example.leica_refactoring.entity.Member;
import com.example.leica_refactoring.error.exception.requestError.ExpiredAccessTokenException;
import com.example.leica_refactoring.error.exception.requestError.ForbiddenException;
import com.example.leica_refactoring.error.exception.requestError.UnAuthorizedException;
import com.example.leica_refactoring.error.security.ErrorCode;
import com.example.leica_refactoring.jwt.JwtTokenProvider;
import com.example.leica_refactoring.repository.MemberRepository;
import com.example.leica_refactoring.service.jwt.RedisService;
import com.example.leica_refactoring.enums.UserRole;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
    private final RedisService redisService;

    public MemberLoginResponseDto login(MemberLoginRequestDto requestDto, HttpServletResponse response) {

        if (!memberRepository.existsByMemberId(requestDto.getMemberId())) {
            throw new UnAuthorizedException("401", ErrorCode.ACCESS_DENIED_EXCEPTION); // 유저를 찾을 수 없을때
        }

        Member member = memberRepository.findByMemberId(requestDto.getMemberId());

        if (!passwordEncoder.matches(requestDto.getPassword(), member.getPassword())) {
            throw new UnAuthorizedException("401", ErrorCode.ACCESS_DENIED_EXCEPTION); // 비밀번호를 틀렸을때
        }

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
        redisService.setValues(memberId, refreshToken);

    }

    public Member findMemberByToken(HttpServletRequest request) {
        String token = jwtTokenProvider.resolveAccessToken(request);

        String accessTokenType = jwtTokenProvider.extractTokenType(token);

        if ("refresh".equals(accessTokenType)) {
            throw new UnAuthorizedException("RefreshToken은 사용할 수 없습니다.", ErrorCode.ACCESS_DENIED_EXCEPTION);
        }

        return token == null ? null : memberRepository.findByMemberId(jwtTokenProvider.getMemberId(token));
    }

    public void reissueToken(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = jwtTokenProvider.resolveRefreshToken(request);

        jwtTokenProvider.validateRefreshToken(refreshToken);

        String newAccessToken = jwtTokenProvider.reissueAccessToken(refreshToken, response);
        String newRefreshToken = jwtTokenProvider.reissueRefreshToken(refreshToken,response);

        jwtTokenProvider.setHeaderAccessToken(response, newAccessToken);
        jwtTokenProvider.setHeaderRefreshToken(response, newRefreshToken);

    }


    public String returnRoleService(HttpServletRequest request) { // 유저 정보 반환 메서드
        String token = jwtTokenProvider.resolveAccessToken(request);

        if (token == null || !checkAccessTokenExpired(request)) {
            throw new ExpiredAccessTokenException("1007",ErrorCode.EXPIRED_ACCESS_TOKEN);
        }

        String tokenRole = jwtTokenProvider.extractTokenRole(token);

        if (tokenRole.equals(UserRole.ADMIN.getKey())) {
            return UserRole.ADMIN.getKey();
        } else if (tokenRole.equals(UserRole.USER.getKey())) {
            return UserRole.USER.getKey();
        } else {
            throw new ForbiddenException("401", ErrorCode.FORBIDDEN_EXCEPTION);
        }

    }

    public boolean checkAccessTokenExpired(HttpServletRequest request) {
        String token = jwtTokenProvider.resolveAccessToken(request);

        if (jwtTokenProvider.validateAccessToken(token)) {
            return true;
        } else if (!jwtTokenProvider.validateAccessToken(token)) {
            return false;
        } else {
            return false;
        }

    }

}
