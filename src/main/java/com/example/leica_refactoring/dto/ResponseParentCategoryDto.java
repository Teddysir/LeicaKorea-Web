package com.example.leica_refactoring.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResponseParentCategoryDto {
    private Long id;
    private String name;
}
