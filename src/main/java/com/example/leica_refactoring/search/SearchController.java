package com.example.leica_refactoring.search;

import com.example.leica_refactoring.dto.PaginationSearchDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "https://localhost:3000")
public class SearchController {

    private final SearchService searchService;
    // 게시물 검색(완성)
    @GetMapping("/search/post")
    public PaginationSearchDto searchPost(@RequestParam(value = "keyword")String keyword, Pageable pageable){
        PaginationSearchDto listDto = searchService.searchPost(keyword, pageable);

        return listDto;
    }
}
