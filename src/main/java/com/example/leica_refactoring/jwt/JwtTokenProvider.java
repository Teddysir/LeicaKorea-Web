package com.example.leica_refactoring.jwt;

import com.example.leica_refactoring.enums.UserRole;
import com.example.leica_refactoring.error.exception.requestError.ExpiredAccessTokenException;
import com.example.leica_refactoring.error.exception.requestError.ExpiredRefreshTokenException;
import com.example.leica_refactoring.error.exception.requestError.ForbiddenException;
import com.example.leica_refactoring.error.security.ErrorCode;
import com.example.leica_refactoring.repository.MemberRepository;
import com.example.leica_refactoring.service.jwt.CustomUserDetailsService;
import com.example.leica_refactoring.service.jwt.RedisService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
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

@Component
@Transactional
@RequiredArgsConstructor
public class JwtTokenProvider {

    private final CustomUserDetailsService customUserDetailsService;
    private final RedisService redisService;
    private final MemberRepository memberRepository;

    @Value("${jwt.secret}")
    private String secretKey;

    private long accessTokenValidTime = 30 * 1000L;
    //    private long refreshTokenValidTime = 7 * 24 * 60 * 60 * 1000L; // 7d
    private long refreshTokenValidTime = 90 * 1000L;

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

    public String reissueAccessToken(String refreshToken, HttpServletResponse response) {
        try {
            String memberId = redisService.getValues(refreshToken);
            return createAccessToken(memberId, memberRepository.findByMemberId(memberId).getUserRole());
        } catch (ExpiredJwtException e) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            return ErrorCode.EXPIRED_ACCESS_TOKEN.getMessage();
        }


    }

    public String reissueRefreshToken(String refreshToken, HttpServletResponse response) {
        try {
            String memberId = redisService.getValues(refreshToken);

            String newRefreshToken = createRefreshToken(memberId, memberRepository.findByMemberId(memberId).getUserRole());

            redisService.delValues(refreshToken);
            redisService.setValues(memberId, newRefreshToken);

            return newRefreshToken;
        } catch (ExpiredJwtException e) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            return ErrorCode.EXPIRED_REFRESH_TOKEN.getMessage();
        }

    }

    public boolean validateRefreshToken(String refreshToken) {
        try {
            Key key = Keys.hmacShaKeyFor(secretKey.getBytes());
            Jws<Claims> claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(refreshToken);

            return !claims.getBody().getExpiration().before(new Date());
        } catch (MalformedJwtException e) {
            throw new MalformedJwtException("Invalid JWT token");
        } catch (ExpiredJwtException e) {
            throw new ExpiredRefreshTokenException("1006", ErrorCode.EXPIRED_REFRESH_TOKEN); // 이부분에 걸리네
        } catch (UnsupportedJwtException ex) {
            throw new UnsupportedJwtException("JWT token is unsupported");
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("JWT claims string is empty");
        }
    }


    public boolean validateAccessToken(String jwtToken) {
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
            throw new ExpiredJwtException(null, null, "AccessToken is Expired");
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
        } else {
            throw new UnsupportedJwtException("JWT token don't have Type");
        }
    }

    public String extractTokenRole(String token) {
        Claims claims = extractClaims(token);
        if (claims != null && claims.containsKey("roles")) {
            return (String) claims.get("roles");
        }
        throw new UnsupportedJwtException("JWT token don't have Roles");
    }

    private Claims extractClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(secretKey.getBytes())
                    .build()
                    .parseClaimsJws(token) // 이 메서드에서 토큰이 만료된걸 추출해야하는데 만료된 토큰의 claims를 추출하려니 오류가 나타나네
                    .getBody();
        } catch (IllegalArgumentException e) {
            throw new UnsupportedJwtException("Can't extract token Type");
        }
    }
}
