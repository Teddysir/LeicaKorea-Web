package com.example.leica_refactoring.dto.search;

import com.example.leica_refactoring.dto.category.RequestChildCategoryDto;
import com.example.leica_refactoring.dto.post.RequestPostDto;
import com.example.leica_refactoring.dto.post.RequestUpdatePostCategoryDto;
import com.example.leica_refactoring.entity.Category;
import lombok.Data;

import javax.persistence.Lob;

@Data
public class RequestPostWithSearchableDto {

    @Lob
    private String searchContent;

    private RequestPostDto post;

    private RequestUpdatePostCategoryDto category;
}
