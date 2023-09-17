package com.example.leica_refactoring.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.List;

@Data
@Builder @Getter
public class ResponseSearchPostDto {

    private Long id;
    private String title;
    private String content; // 추가한 필드
    private String subTitle;
    private String thumbnail;
    private String parentName;
    private String childName;

}
