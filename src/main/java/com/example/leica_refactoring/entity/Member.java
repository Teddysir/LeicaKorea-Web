package com.example.leica_refactoring.entity;

import com.example.leica_refactoring.jwt.UserRole;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;

/**
 *  1. admin
 *  2. user
 *
 *  memberId, pw, userName, role,
 */

@Entity
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
    private String password;

    @Column(nullable = false)
    private String username;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole userRole;

    @Builder
    public Member(Long id, String memberId, String password, String username) {
        this.id = id;
        this.memberId = memberId;
        this.password = password;
        this.username = username;
    }
}
