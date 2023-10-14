package com.example.leica_refactoring.entity;

import com.example.leica_refactoring.Auth.annotation.Authority;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import java.util.Set;

/**
 *  1. admin
 *  2. user
 *
 *  memberId, pw, userName, role,
 */

@Entity
@Builder
@Getter
@NoArgsConstructor
public class Member {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "m_id")
    private Long id;

    @Column(nullable = false, unique = true)
    @Length(min = 3, max = 20)
    private String memberId;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @ManyToMany
    @JoinTable(
            name = "user_authority",
            joinColumns = {@JoinColumn(name = "m_id", referencedColumnName = "m_id")},
            inverseJoinColumns = {@JoinColumn(name = "authority_name", referencedColumnName = "authority_name")})
    private Set<Authority> authorities;

    @Builder
    public Member(Long id, String memberId, String password, String username, Set<Authority> authorities) {
        this.id = id;
        this.memberId = memberId;
        this.password = password;
        this.username = username;
        this.authorities = authorities;
    }
}
