package com.example.leica_refactoring.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@Builder @NoArgsConstructor @AllArgsConstructor
public class SearchPost {

    @Id @GeneratedValue
    private Long id;

    @OneToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @Lob
    @Column(nullable = false)
    private String searchContent;

}
