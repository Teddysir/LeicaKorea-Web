package com.example.leica_refactoring.dto.post;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@JsonDeserialize(builder = RequestPostDto.Builder.class)
public class RequestPostDto {

    @Schema(description = "제목", example = "광학 현미경")
    private String title;
    @Schema(description = "내용", example = "광학 현미경 내용")
    private String content;
    @Schema(description = "서브타이틀", example = "서브타이틀")
    private String subTitle;
    @Schema(description = "썸네일", example = "썸네일")
    private String thumbnail;
    @Schema(description = "현재 부모 카테고리", example = "현재 부모 카테고리")
    private String parentName;
    @Schema(description = "현재 자식 카테고리", example = "현재 자식 카테고리")
    private String childName;


    private RequestPostDto(String title, String subTitle, String content, String thumbnail, String parentName, String childName) {
        this.title = title;
        this.subTitle = subTitle;
        this.content = content;
        this.thumbnail = thumbnail;
        this.parentName = parentName;
        this.childName = childName;
    }

    @JsonPOJOBuilder
    static class Builder {
        String title;
        String content;
        String subtitle;
        String thumbnail;
        String parentName;
        String childName;

        Builder withTitle(String title) {
            this.title = title;
            return this;
        }

        Builder withContent(String content) {
            this.content = content;
            return this;
        }

        Builder withThumbnail(String thumbnail) {
            this.thumbnail = thumbnail;
            return this;
        }

        Builder withParentName(String parentName) {
            this.parentName = parentName;
            return this;
        }

        Builder withChildName(String childName) {
            this.childName = childName;
            return this;
        }

        Builder withSubTitle(String subTitle) {
            this.subtitle = subTitle;
            return this;
        }

        public RequestPostDto build() {
            return new RequestPostDto(title, subtitle, content, thumbnail, parentName, childName);
        }
    }

}

