package com.example.leica_refactoring.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class RequestUpdateChildCategoryDto {
    
    private String categoryName;

    public void RequestChildCategoryDto(String categoryName){
        this.categoryName = categoryName;
    }
}
