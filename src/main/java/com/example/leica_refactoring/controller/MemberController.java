package com.example.leica_refactoring.controller;

import com.example.leica_refactoring.jwt.JwtAuthenticationTokenFilter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import com.example.leica_refactoring.dto.member.MemberLoginRequestDto;
import com.example.leica_refactoring.dto.member.MemberLoginResponseDto;
import com.example.leica_refactoring.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "Member Controller", description = "Member API")
@CrossOrigin(origins = {"https://localhost:3000","https://www.nts-microscope.com"})
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/login")
    @Operation(summary = "로그인")
    public MemberLoginResponseDto login(@RequestBody MemberLoginRequestDto requestDto, HttpServletResponse response) {
        return memberService.login(requestDto, response);
    }

    @GetMapping("/reissue")
    @Operation(summary = "토큰 재발급")
    public ResponseEntity<String> reissueToken(HttpServletRequest request, HttpServletResponse response) {
        memberService.reissueToken(request, response);
        return ResponseEntity.ok("토큰 재발급 완료");
    }

    @GetMapping("/info")
    @Operation(summary = "유저 정보 반환")
    public String returnRole(HttpServletRequest request) {
        return memberService.returnRoleService(request);
    }

    @GetMapping("/expired")
    @Operation(summary = "AccessToken 만료 여부 반환")
    public ResponseEntity<Object> returnExpired(HttpServletRequest request) {
        boolean isTokenValid = memberService.checkAccessTokenExpired(request);

        if (isTokenValid) {
            return ResponseEntity.ok().build(); // Returns HTTP 200 OK
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // Or any other HTTP status code you want to return
        }
    }

}
