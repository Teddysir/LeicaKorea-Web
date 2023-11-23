package com.example.leica_refactoring.member;

import com.example.leica_refactoring.entity.Member;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;
import java.util.List;


public class MemberAccount extends User {
    public MemberAccount(Member member) {
        super(member.getMemberId(), member.getPassword(), List.of(new SimpleGrantedAuthority("ROLE_USER")));
    }
}