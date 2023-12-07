package com.example.leica_refactoring.service;

import com.example.leica_refactoring.dto.search.PaginationSearchDto;
import com.example.leica_refactoring.dto.search.ResponseSearchPostDto;
import com.example.leica_refactoring.entity.Post;
import com.example.leica_refactoring.entity.SearchPost;
import com.example.leica_refactoring.error.exception.requestError.BadRequestException;
import com.example.leica_refactoring.error.security.ErrorCode;
import com.example.leica_refactoring.repository.PostRepository;
import com.example.leica_refactoring.repository.SearchRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class SearchService {

    private final SearchRepository searchRepository;

    @Transactional
    public PaginationSearchDto searchPost(String keyword, Pageable pageable) {

        Page<SearchPost> searchPostPage = searchRepository.findBySearchContentContainingOrPostTitleContaining(keyword, keyword, pageable);

        if (searchPostPage == null) {
            throw new BadRequestException("잘못된 요청입니다.", ErrorCode.RUNTIME_EXCEPTION);
        }

        List<ResponseSearchPostDto> collect = searchPostPage.stream().map(searchPost -> {
            String content = searchPost.getSearchContent();
            Post post = searchPost.getPost();
            Pattern compile = Pattern.compile("[^/]*" + keyword + "[^/]*");
            Matcher matcher = compile.matcher(content);
            StringBuilder matchedContent = new StringBuilder();

            while (matcher.find()) {
                matchedContent.append(matcher.group()).append(" ");
            }

            if (matchedContent.toString().trim().equals("")) {
                content = content.replace("/", "");
                content = content.substring(0, Math.min(content.length(), 30));
            } else {
                content = matchedContent.toString().trim();
                content = content.replace("/", "");
            }

            return ResponseSearchPostDto.builder()
                    .id(post.getId())
                    .title(post.getTitle())
                    .subTitle(post.getSubTitle())
                    .content(content)
                    .thumbnail(post.getThumbnail())
                    .createdAt(post.getCreatedAt())
                    .modified_at(post.getModified_at())
                    .parentName(post.getChildCategory().getParent().getName())
                    .childName(post.getChildCategory().getName())
                    .build();
        }).collect(Collectors.toList());

        long size = searchPostPage.getTotalElements();
        boolean isLastPage = !searchPostPage.hasNext();
        long totalPages = searchPostPage.getTotalPages();

        PaginationSearchDto build = PaginationSearchDto.builder()
                .lastPage(isLastPage)
                .totalPage(totalPages)
                .totalElement(size)
                .childList(collect)
                .build();

        return build;
    }


}
