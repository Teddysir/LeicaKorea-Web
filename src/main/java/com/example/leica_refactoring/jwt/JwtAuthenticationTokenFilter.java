package com.example.leica_refactoring.jwt;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
@Transactional
public class JwtAuthenticationTokenFilter {
}
