package com.example.leica_refactoring.Auth.service;

import com.example.leica_refactoring.Auth.dto.SignUpRequestDto;
import com.example.leica_refactoring.Auth.dto.UserResDto;
import com.example.leica_refactoring.Auth.annotation.Authority;
import com.example.leica_refactoring.Auth.annotation.AuthorityName;
import com.example.leica_refactoring.entity.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;

    @Transactional
    public String signUp(SignUpRequestDto dto){

        Set<Authority> authorities = new HashSet<>();

        authorities.add(new Authority(AuthorityName.ROLE_USER)); // 기본으론 USER권한만 부여가능 관리자만 ADMIN 계정 추가 가능하다.
//        for(String authorityStr : dto.getAuthorities()) {
//            switch(authorityStr) {
//                case "ROLE_USER":
//                    authorities.add(new Authority(AuthorityName.ROLE_USER));
//                    break;
//                case "ROLE_ADMIN":
//                    authorities.add(new Authority(AuthorityName.ROLE_ADMIN));
//                    break;
//                default:
//                    break;
//            }
//        }

        Member member = Member.builder()
                .memberId(dto.getMemberId())
                .password(passwordEncoder.encode(dto.getPassword()))
                .username(dto.getUsername())
                .authorities(authorities)
                .build();

        memberRepository.save(member);

        return "sign up success";
    }

    @Transactional(readOnly = true)
    public UserResDto getMemberId() {
            CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Long id = userDetails.getId();

            Member member = memberRepository.findById(id).orElseThrow(()-> new NoSuchElementException());

        return UserResDto.builder()
                .memberId(member.getMemberId())
                .username(member.getUsername())
                .build();
    }
}
