package com.example.leica_refactoring.jwt;

import com.example.leica_refactoring.config.SecurityConfig;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.IOException;
import java.security.SignatureException;

@Slf4j
@Component
@RequiredArgsConstructor
@Transactional
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String accessToken = jwtTokenProvider.resolveAccessToken(request);
        String refreshToken = jwtTokenProvider.resolveRefreshToken(request);

        try {
            if (accessToken == null && refreshToken != null) {
                if (jwtTokenProvider.validateToken(refreshToken)) {
                    filterChain.doFilter(request, response);
                    return;
                }
            } else if (accessToken == null && refreshToken == null) {
                filterChain.doFilter(request, response);
                return;
            } else {
                if (jwtTokenProvider.validateToken(accessToken)) {
                    this.setAuthentication(accessToken);
                }
            }
        } catch (IllegalArgumentException e) {
            return;
        }

        filterChain.doFilter(request, response);
    }

    private void setAuthentication(String token) {
        Authentication authentication = jwtTokenProvider.getAuthentication(token);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
