package com.example.leica_refactoring.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.security.Key;
import java.util.Base64;
import java.util.*;

@Slf4j
@Component
@Transactional
@RequiredArgsConstructor
public class JwtTokenProvider {

    private final MemberRepository memberRepository;
    private final CustomUserDetailsService customUserDetailsService;

    @Value("${jwt.secret}")
    private String secretKey;

    private long accessTokenValidTime = 60 * 60 * 1000L; // 1h
    private long refreshTokenValidTime = 7 * 24 * 60 * 60 * 1000L; // 7d

    public void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    public String createdAccessToken(String memberId, UserRole userRole) {
        return this.createToken(memberId, userRole, accessTokenValidTime);
    }

    public String createdRefreshToken(String memberId, UserRole userRole) {
        return this.createToken(memberId, userRole, refreshTokenValidTime);
    }

    // 토큰 생성 로직
    public String createToken(String memberId, UserRole userRole, long tokenValid) {
        Claims claims = Jwts.claims().setSubject(memberId);
        claims.put("roles", userRole.toString()); // key - value 형태로 저장하는거임 claims는 jwt에 들어가는 정보를 key-value 형식으로 저장함 memberId가 주체임

        Key key = Keys.hmacShaKeyFor(secretKey.getBytes());
        Date date = new Date();

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(date)
                .setExpiration(new Date(date.getTime() + tokenValid))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public UsernamePasswordAuthenticationToken getAuthentication(String token) {
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(this.getMemberId(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public String getMemberId(String token) {
        JwtParser jwtParser = Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .build();

        return jwtParser.parseClaimsJws(token).getBody().getSubject();
    }

    public String resolveRefreshToken(HttpServletRequest request) {
        if(request.getHeader("refreshToken") != null){
            return request.getHeader("refreshToken").substring(7);
        }
        return null;
    }

    public String resolveAccessToken(HttpServletRequest request) {
        if(request.getHeader("accessToken") != null){
            return request.getHeader("accessToken").substring(7);
        }
        return null;
    }

    public boolean validateToken(String jwtToken) {
        try {
            Key key = Keys.hmacShaKeyFor(secretKey.getBytes());
            Jws<Claims> claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(jwtToken);

            return !claims.getBody().getExpiration().before(new Date());
        } catch (MalformedJwtException e) {
            throw new MalformedJwtException("Invalid JWT token");
        } catch (ExpiredJwtException e) {
            throw new ExpiredJwtException(null, null, "Token has expired");
        } catch (UnsupportedJwtException e) {
            throw new UnsupportedJwtException("JWT token is unsupported");
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("JWT claims string is empty");
        }
    }

}
