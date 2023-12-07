package com.example.leica_refactoring.entity;


import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchPost {

    @Id
    @GeneratedValue
    private Long id;

    @OneToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @Lob
    @Column(nullable = false)
    private String searchContent;

}
