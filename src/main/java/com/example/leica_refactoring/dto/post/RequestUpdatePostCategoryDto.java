package com.example.leica_refactoring.dto.post;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@JsonDeserialize(builder = RequestUpdatePostCategoryDto.Builder.class)
public class RequestUpdatePostCategoryDto {

    @Schema(description = "제목", example = "광학 현미경")
    private String title;
    @Schema(description = "내용", example = "광학 현미경 내용")
    private String content;
    @Schema(description = "서브타이틀", example = "서브타이틀")
    private String subTitle;
    @Schema(description = "썸네일", example = "썸네일")
    private String thumbnail;
    @Schema(description = "부모 카테고리", example = "부모 카테고리")
    private Long parentId;
    @Schema(description = "자식 카테고리", example = "자식 카테고리")
    private Long childId;


    private RequestUpdatePostCategoryDto(String title, String subTitle, String content, String thumbnail, Long parentId, Long childId) {
        this.title = title;
        this.subTitle = subTitle;
        this.content = content;
        this.thumbnail = thumbnail;
        this.parentId = parentId;
        this.childId = childId;
    }

    @JsonPOJOBuilder
    static class Builder {
        String title;
        String content;
        String subtitle;
        String thumbnail;
        Long parentId;
        Long childId;

        Builder withTitle(String title) {
            this.title = title;
            return this;
        }

        Builder withSubTitle(String subTitle) {
            this.subtitle = subTitle;
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

        Builder withParentId(Long parentId) {
            this.parentId = parentId;
            return this;
        }

        Builder withChildId(Long childId) {
            this.childId = childId;
            return this;
        }

        public RequestUpdatePostCategoryDto build() {
            return new RequestUpdatePostCategoryDto(title, subtitle, content, thumbnail, parentId, childId);
        }
    }
}
