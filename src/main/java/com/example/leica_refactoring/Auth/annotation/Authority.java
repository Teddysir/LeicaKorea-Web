package com.example.leica_refactoring.Auth.annotation;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
@Entity
@Getter
@NoArgsConstructor
public class Authority {
    @Id
    @Column(name = "authority_name")
    @Enumerated(EnumType.STRING)
    private AuthorityName authorityName;

    public Authority(AuthorityName authorityName) {
        this.authorityName = authorityName;
    }
}