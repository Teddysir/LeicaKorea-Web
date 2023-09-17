package com.example.leica_refactoring.search;

import com.example.leica_refactoring.dto.ResponseSearchPostListDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;
    // 게시물 검색(완성)
    @GetMapping("/search/post")
    public ResponseSearchPostListDto searchPost(@RequestParam(value = "keyword")String keyword){
        ResponseSearchPostListDto listDto = searchService.searchPost(keyword);

        return listDto;
    }
}
