package com.example.leica_refactoring.controller;

import com.example.leica_refactoring.dto.search.PaginationSearchDto;
import com.example.leica_refactoring.service.SearchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = {"https://localhost:3000", "https://www.nts-microscope.com"})
@Tag(name = "Search Controller", description = "Search API")
public class SearchController {

    private final SearchService searchService;

    // 게시물 검색(완성)
    @GetMapping("/search")
    @Operation(summary = "게시물 검색")
    public PaginationSearchDto searchPost(@RequestParam(value = "keyword") String keyword, Pageable pageable) {
        PaginationSearchDto listDto = searchService.searchPost(keyword, pageable);

        return listDto;
    }

}
