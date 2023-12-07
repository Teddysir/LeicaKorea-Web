package com.example.leica_refactoring.dto.post;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaginationDto {

    private Long totalPage;
    private Boolean lastPage;
    private Long totalElement;
    private List<ResponsePostDto> childList = new ArrayList<>();
}
