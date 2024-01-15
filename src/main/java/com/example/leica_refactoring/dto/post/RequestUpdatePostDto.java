package com.example.leica_refactoring.dto.post;

import lombok.Data;

import javax.persistence.Lob;

@Data
public class RequestUpdatePostDto {

    @Lob
    private String searchContent;

    private RequestUpdatePostCategoryDto post;

}
