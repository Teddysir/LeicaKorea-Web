package com.example.leica_refactoring.Auth.service;

import lombok.Builder;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Getter
public class CustomUserDetails implements UserDetails {
    private Long id;
    private String memberId;
    private String password;
    private Collection<GrantedAuthority> authorities;

    @Builder
    public CustomUserDetails(Long id, String memberId, String password, Collection<GrantedAuthority> authorities) {
        this.id = id;
        this.memberId = memberId;
        this.password = password;
        this.authorities = authorities;
    }

    @Override
    public String getUsername() {
        return memberId;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
