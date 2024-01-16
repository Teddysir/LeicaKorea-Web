package com.example.leica_refactoring.dto.category;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResponseParentCategoryDto {
    private Long parentId;
    private String parentName;
}
