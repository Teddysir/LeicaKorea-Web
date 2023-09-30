package com.example.leica_refactoring.search;

import com.example.leica_refactoring.dto.ResponsePostDto;
import com.example.leica_refactoring.dto.ResponsePostListDto;
import com.example.leica_refactoring.dto.ResponseSearchPostDto;
import com.example.leica_refactoring.dto.ResponseSearchPostListDto;
import com.example.leica_refactoring.entity.Post;
import com.example.leica_refactoring.entity.SearchPost;
import com.example.leica_refactoring.post.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
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
    private final PostRepository postRepository;

    @Transactional
    public ResponseSearchPostListDto searchPost(String keyword) {
        List<SearchPost> postList = searchRepository.findBySearchContentContainingOrPostTitleContaining(keyword, keyword);

        List<ResponseSearchPostDto> collect = postList.stream().map(searchPost -> {
            String content = searchPost.getSearchContent();
            Post post = searchPost.getPost();
            Pattern compile = Pattern.compile("[^/]*"+keyword+"[^/]*");
            Matcher matcher = compile.matcher(content);
            StringBuilder matchedContent = new StringBuilder();

            while (matcher.find()) {
                matchedContent.append(matcher.group()).append(" ");
            }

            if (matchedContent.toString().trim().equals("")){
                content  = content.replace("/", "");
                content = content.substring(0, Math.min(content.length(), 30));
            }else{
                 content = matchedContent.toString().trim();
                 content = content.replace("/", "");
            }

            return ResponseSearchPostDto.builder()
                    .id(post.getId())
                    .title(post.getTitle())
                    .subTitle(post.getSubTitle())
                    .content(content)
                    .thumbnail(post.getThumbnail())
                    .parentName(post.getChildCategory().getParent().getName())
                    .childName(post.getChildCategory().getName())
                    .build();
        }).collect(Collectors.toList());

        long size = collect.size();

        ResponseSearchPostListDto build = ResponseSearchPostListDto.builder()
                .size(size)
                .childList(collect)
                .build();
        return build;
    }


}
