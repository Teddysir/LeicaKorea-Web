//package com.example.leica_refactoring.jwt;
//
//
//import com.example.leica_refactoring.entity.Member;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Component;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Component("userDetailsService")
//@RequiredArgsConstructor
//public class CustomUserDetailsService implements UserDetailsService {
//    private final MemberRepository memberRepository;
//
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
//        String memberId = username;
//        Member member = memberRepository.findUsernameWithAuthoritiesByMemberId(memberId).orElseThrow(() -> new UsernameNotFoundException("No Member"));
//
//        List<GrantedAuthority> grantedAuthorities  = member.getAuthorities().stream()
//                .map(authority -> new SimpleGrantedAuthority(authority.getAuthorityName().toString()))
//                .collect(Collectors.toList());
//
//        return CustomUserDetails.builder()
//                .id(member.getId())
//                .memberId(member.getMemberId())
//                .password(member.getPassword())
//                .authorities(grantedAuthorities)
//                .build();
//    }
//}
