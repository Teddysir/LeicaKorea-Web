package com.example.leica_refactoring.Auth.auth;

import com.example.leica_refactoring.Auth.service.AuthService;
import com.example.leica_refactoring.Auth.service.MemberRepository;
import com.example.leica_refactoring.Auth.dto.SignUpRequestDto;
import com.example.leica_refactoring.Auth.service.UserService;
import com.example.leica_refactoring.dto.RequestLoginDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody @Validated RequestLoginDto dto, HttpServletResponse response){
        String msg = authService.login(dto,response);
        return new ResponseEntity<>(msg, HttpStatus.OK);
    }

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody @Validated SignUpRequestDto signUpReqDto) {
        String res = userService.signUp(signUpReqDto);
        return new ResponseEntity<>(res, HttpStatus.CREATED);
    }
}
