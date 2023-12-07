package com.example.leica_refactoring.service.jwt;

import com.example.leica_refactoring.entity.Member;
import com.example.leica_refactoring.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;


@Component("userDetailsService")
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = memberRepository.findByMemberId(username);

        if (member == null) {
            throw new UsernameNotFoundException(username + "는 존재하지 않는 사용자입니다.");
        }

        return new CustomUserDetails(member);

    }
}
