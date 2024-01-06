package com.example.leica_refactoring.dto.post;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
public class RequestUpdatePostCategoryDto {

    @Schema(description = "바꿀 부모카테고리 이름", example = "바꿀 부모 카테고리")
    private String parentName;
    @Schema(description = "바꿀 자식카테고리 이름", example = "바꿀 자식 카테고리")
    private String childName;

    private RequestUpdatePostCategoryDto(String parentName, String childName) {
        this.parentName = parentName;
        this.childName = childName;
    }
}
