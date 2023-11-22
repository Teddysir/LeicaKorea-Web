package com.example.leica_refactoring.Auth.service;

import com.example.leica_refactoring.Auth.exception.LoginFailException;
import com.example.leica_refactoring.dto.RequestLoginDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.session.data.redis.RedisSessionRepository;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    public String login(RequestLoginDto loginDto, HttpServletResponse response, HttpServletRequest request){
        try {
            // authenticationToken 인증용 객체
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginDto.getMemberId(),loginDto.getPassword());
            // .authenticate(): 접근 주체 인증(CustomUserDetailsService의 loadUserByUsername 실행)
            // 인증이 완료된 경우 authentication 객체를 반환
            Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
            // authentication 객체 세션에 저장
            SecurityContextHolder.getContext().setAuthentication(authentication);

            String sessionId = UUID.randomUUID().toString();

            ResponseCookie sessionCookie = ResponseCookie.from("my-cookie","my-value")
                    .secure(true)
                    .sameSite("None")
                    .httpOnly(true)
                    .build();

            response.addHeader("Set-Cookie",sessionCookie.toString());

            HttpSession session = request.getSession();
            session.setAttribute("sessionId", sessionId);

            return "Login Success";
        } catch (Exception e) {
            throw new LoginFailException("Login Fail");
        }
    }
}
