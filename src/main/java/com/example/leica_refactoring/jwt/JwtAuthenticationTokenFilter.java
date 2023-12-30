package com.example.leica_refactoring.jwt;

import com.example.leica_refactoring.error.security.ErrorCode;
import com.example.leica_refactoring.error.security.ErrorJwtCode;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.IOException;

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
        String path = request.getRequestURI();
        ErrorJwtCode errorCode;

        try {
            if (accessToken == null && refreshToken != null) { // accessToken이 없고 refreshToken 만 유효할때
                if (!jwtTokenProvider.validateToken(refreshToken) && path.contains("/reissue")) { // refreshToken이 유효하지않으면
                    errorCode = ErrorJwtCode.EXPIRED_REFRESH_TOKEN;
                    setResponse(response, errorCode);
                    return;
                }
                if (jwtTokenProvider.validateToken(refreshToken) && path.contains("/reissue")) {
                    filterChain.doFilter(request, response);
                }
            } else if (accessToken == null && refreshToken == null) { // accessToken, RefreshToken 둘다 없을때
                filterChain.doFilter(request, response);
                return;
            } else {
                try {
                    jwtTokenProvider.validateToken(accessToken); // accessToken 유효성 검사해 true면 쓰는데 false면 catch로 넘어가
                    this.setAuthentication(accessToken);
                } catch (ExpiredJwtException e) {
                    if (refreshToken != null && jwtTokenProvider.validateToken(refreshToken)) { // 만약 rt가 null이 아니고 유효하면
                        accessToken = jwtTokenProvider.reissueAccessToken(refreshToken); // 유효성 검사해서 at 재발급
                        this.setAuthentication(accessToken);
                        filterChain.doFilter(request, response);
                    } else { // 아니면 그냥 at 만료
                        setResponse(response, ErrorJwtCode.EXPIRED_ACCESS_TOKEN);
                    }
                    return;
                }
            }
        } catch (MalformedJwtException e) {
            errorCode = ErrorJwtCode.INVALID_JWT_FORMAT;
            setResponse(response, errorCode);
            return;
        } catch (ExpiredJwtException e) {
            String tokenType = jwtTokenProvider.extractTokenType(accessToken);
            if ("access".equals(tokenType)) {
                setResponse(response, ErrorJwtCode.EXPIRED_ACCESS_TOKEN);
            } else {
                setResponse(response, ErrorJwtCode.EXPIRED_REFRESH_TOKEN);
            }
            return;
        } catch (UnsupportedJwtException e) {
            errorCode = ErrorJwtCode.UNSUPPORTED_JWT_TOKEN;
            setResponse(response, errorCode);
            return;
        } catch (IllegalArgumentException e) {
            errorCode = ErrorJwtCode.INVALID_VALUE;
            setResponse(response, errorCode);
            return;
        } catch (RuntimeException e) {
            errorCode = ErrorJwtCode.RUNTIME_EXCEPTION;
            setResponse(response, errorCode);
            return;
        }

        filterChain.doFilter(request, response);
    }

    private void setAuthentication(String token) {
        Authentication authentication = jwtTokenProvider.getAuthentication(token);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private void setResponse(HttpServletResponse response, ErrorJwtCode errorCode) throws IOException {
        JSONObject json = new JSONObject();
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        json.put("code", errorCode.getCode());
        json.put("message", errorCode.getMessage());

        response.getWriter().print(json);
        response.getWriter().flush();

    }
}
