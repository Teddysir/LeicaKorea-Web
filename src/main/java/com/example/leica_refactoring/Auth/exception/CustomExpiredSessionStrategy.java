package com.example.leica_refactoring.Auth.exception;

import com.example.leica_refactoring.Auth.service.MemberRepository;
import com.example.leica_refactoring.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.server.Session;
import org.springframework.http.HttpStatus;
import org.springframework.security.web.session.SessionInformationExpiredEvent;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@RequiredArgsConstructor
public class CustomExpiredSessionStrategy implements SessionInformationExpiredStrategy  {

    @Override
    public void onExpiredSessionDetected(SessionInformationExpiredEvent event) throws IOException {
        HttpServletRequest request = event.getRequest();
        HttpServletResponse response = event.getResponse();
        HttpSession session = request.getSession();

        session.setAttribute("중복 로그인입니다.",true);

        response.sendRedirect("/login");
    }
}
