package com.example.leica_refactoring.dto.category;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
public class RequestChildCategoryDto {
    private Long parentId;
    private String childName;

    private RequestChildCategoryDto(Long parentId, String childName) {
        this.parentId = parentId;
        this.childName = childName;
    }
}
