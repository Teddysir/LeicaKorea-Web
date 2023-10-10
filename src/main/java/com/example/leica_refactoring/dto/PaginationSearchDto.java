package com.example.leica_refactoring.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor @AllArgsConstructor
public class PaginationSearchDto {
    private Long totalPage;
    private Boolean lastPage;
    private Long totalElement;
    private List<ResponseSearchPostDto> childList = new ArrayList<>();
}
