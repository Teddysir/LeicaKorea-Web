package com.example.leica_refactoring.dto;

import lombok.Data;

import javax.persistence.Lob;

@Data
public class RequestPostWithSearchableDto {

    @Lob
    private String content;

    private RequestPostDto post;
}
