package com.example.leica_refactoring.entity;

import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Post extends PostTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @OnDelete(action = OnDeleteAction.CASCADE)
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

    @OneToOne(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true) // post 필드를 통해 양방향 관계를 맺게해줌
    private SearchPost searchPost; // CASCADE 설정도 해주었다.

}
