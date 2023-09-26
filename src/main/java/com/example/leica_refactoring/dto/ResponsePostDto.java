package com.example.leica_refactoring.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResponsePostDto {

    private Long id;
    private String title;
    private String content;
    private String subTitle;
    private String thumbnail;
    private String category;

}
