package com.example.leica_refactoring.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestParentCategoryDto {
    private String parentName;

//    User user = userService.findUserByToken(request);
//    Store store = storeRepository.findById(id).orElseThrow();
}
