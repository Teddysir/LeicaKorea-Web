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
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    public String login(RequestLoginDto loginDto, HttpServletResponse response){
        try {
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginDto.getMemberId(),loginDto.getPassword());
            Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            String sessionId = UUID.randomUUID().toString();

// Create a session cookie with the session ID
            ResponseCookie sessionCookie = ResponseCookie.from("my-cookie", sessionId)
                    .secure(true)
                    .sameSite("None")
                    .httpOnly(true)
                    .build();

// Add the session cookie to the response headers
            response.addHeader("Set-Cookie", sessionCookie.toString());
//            ResponseCookie cookie = ResponseCookie.from("my-cookie",)
//                    .secure(true)
//                    .sameSite("None")
//                    .httpOnly(true)
//                    .build();
//
//            response.addHeader("Set-Cookie",cookie.toString());

            return "Login Success";
        } catch (Exception e) {
            throw new LoginFailException("Login Fail");
        }
    }
}
