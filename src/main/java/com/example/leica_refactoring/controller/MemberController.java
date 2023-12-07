package com.example.leica_refactoring.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import com.example.leica_refactoring.dto.member.MemberLoginRequestDto;
import com.example.leica_refactoring.dto.member.MemberLoginResponseDto;
import com.example.leica_refactoring.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


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
}
