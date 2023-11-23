package com.example.leica_refactoring.entity;

import lombok.AllArgsConstructor;
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
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Member {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "m_id")
    private Long id;

    @Column(nullable = false, unique = true)
    @Length(min = 3, max = 20)
    private String memberId;

    @Column(nullable = false)
    @Length(min = 1, max = 20)
    private String username;

    @Column(nullable = false)
    private String password;
}