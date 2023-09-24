package com.example.leica_refactoring.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter @Setter
@Builder
@NoArgsConstructor @AllArgsConstructor
public class Post extends PostTime {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne()
    @JoinColumn(name = "m_id")
    private Member member;

    @Column(nullable = false)
    private String title;


    private String subTitle;

    @Lob
    @Column(nullable = false)
    private String content;

    private String writer;

    @Lob
    private String thumbnail;

    @ManyToOne
    private Category childCategory;


}
