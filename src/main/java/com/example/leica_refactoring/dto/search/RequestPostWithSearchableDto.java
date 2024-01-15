package com.example.leica_refactoring.dto.search;

import com.example.leica_refactoring.dto.post.RequestPostDto;
import lombok.Data;

import javax.persistence.Lob;

@Data
public class RequestPostWithSearchableDto {

    @Lob
    private String searchContent;

    private RequestPostDto post;

}
