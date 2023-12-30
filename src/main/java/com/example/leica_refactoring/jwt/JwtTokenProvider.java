package com.example.leica_refactoring.jwt;

import com.example.leica_refactoring.enums.UserRole;
import com.example.leica_refactoring.error.exception.requestError.ForbiddenException;
import com.example.leica_refactoring.error.security.ErrorCode;
import com.example.leica_refactoring.repository.MemberRepository;
import com.example.leica_refactoring.service.jwt.CustomUserDetailsService;
import com.example.leica_refactoring.service.jwt.RedisService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.Key;
import java.util.Base64;
import java.util.*;

@Slf4j
@Component
@Transactional
@RequiredArgsConstructor
public class JwtTokenProvider {

    private final CustomUserDetailsService customUserDetailsService;
    private final RedisService redisService;
    private final MemberRepository memberRepository;

    @Value("${jwt.secret}")
    private String secretKey;

    private long accessTokenValidTime = 30 * 1000L; // 1h
    //    private long refreshTokenValidTime = 7 * 24 * 60 * 60 * 1000L; // 7d
    private long refreshTokenValidTime = 60 * 1000L;

    @PostConstruct
    public void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    public String createAccessToken(String memberId, UserRole userRole) {
        return this.createToken(memberId, userRole, accessTokenValidTime, "access");
    }

    public String createRefreshToken(String memberId, UserRole userRole) {
        return this.createToken(memberId, userRole, refreshTokenValidTime, "refresh");
    }

    // 토큰 생성 로직
    public String createToken(String memberId, UserRole userRole, long tokenValid, String tokenType) {
        Claims claims = Jwts.claims().setSubject(memberId);
        claims.put("roles", userRole.toString()); // key - value 형태로 저장하는거임 claims는 jwt에 들어가는 정보를 key-value 형식으로 저장함 memberId가 주체임
        claims.put("type", tokenType);

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

    public String resolveAccessToken(HttpServletRequest request) {
        if (request.getHeader("authorization") != null)
            return request.getHeader("authorization").substring(7);
        return null;
    }

    public String resolveRefreshToken(HttpServletRequest request) {
        if (request.getHeader("refreshToken") != null)
            return request.getHeader("refreshToken").substring(7);
        return null;
    }

    public String reissueAccessToken(String refreshToken) {
        String memberId = redisService.getValues(refreshToken);

        if (memberId == null) {
            throw new ForbiddenException("401", ErrorCode.ACCESS_DENIED_EXCEPTION);
        }

        return createAccessToken(memberId, memberRepository.findByMemberId(memberId).getUserRole());
    }

    public String reissueRefreshToken(String refreshToken) {
        String memberId = redisService.getValues(refreshToken);
        if (Objects.isNull(memberId)) {
            throw new ForbiddenException("401", ErrorCode.ACCESS_DENIED_EXCEPTION);
        }

        String newRefreshToken = createRefreshToken(memberId, memberRepository.findByMemberId(memberId).getUserRole());

        redisService.delValues(refreshToken);
        redisService.setValues(newRefreshToken, memberId);

        return newRefreshToken;
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
            throw new ExpiredJwtException(null,null, "Expired JWT TOKEN");
        } catch (UnsupportedJwtException ex) {
            throw new UnsupportedJwtException("JWT token is unsupported");
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("JWT claims string is empty");
        }
    }

    public void setHeaderAccessToken(HttpServletResponse response, String accessToken) {
        response.setHeader("authorization", "Bearer " + accessToken);
    }

    public void setHeaderRefreshToken(HttpServletResponse response, String refreshToken) {
        response.setHeader("refreshToken", "Bearer " + refreshToken);
    }

    public String extractTokenType(String token) {
        Claims claims = extractClaims(token);
        if (claims != null && claims.containsKey("type")) {
            return (String) claims.get("type");
        }
        throw new UnsupportedJwtException("JWT token don't have Type");
    }

    public String extractTokenRole(String token) {
        Claims claims =extractClaims(token);
        if(claims!= null && claims.containsKey("roles")) {
            return (String) claims.get("roles");
        }
        throw new UnsupportedJwtException("JWT token don't have Roles");
    }

    private Claims extractClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(secretKey.getBytes())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (IllegalArgumentException e) {
            throw new UnsupportedJwtException("Can't extract token Type");
        }
    }
}
