package com.example.leica_refactoring.dto.category;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PaginationCategoryDto {

    private Long totalPage;
    private Boolean lastPage;
    private Long totalElement;
    private List<ResponseChildCategoryDto> childCategoryDtos = new ArrayList<>();
}
