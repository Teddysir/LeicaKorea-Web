package com.example.leica_refactoring.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import software.amazon.ion.IntegerSize;

import java.util.List;

@Data
@Builder
@NoArgsConstructor @AllArgsConstructor
public class ResponseChildCategoryDto {
    private Long id;
    private String childName;
    private int size;
}
