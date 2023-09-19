package com.example.leica_refactoring.dto;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResponsePostOneDto {

    private Long id;
    private String title;
    private String subTitle;
    private String content;
    private String thumbnail;
    private String writer;
    private String category;
    private String parentCategory;

}
