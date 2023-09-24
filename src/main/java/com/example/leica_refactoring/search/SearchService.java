package com.example.leica_refactoring.search;

import com.example.leica_refactoring.dto.ResponsePostDto;
import com.example.leica_refactoring.dto.ResponsePostListDto;
import com.example.leica_refactoring.dto.ResponseSearchPostDto;
import com.example.leica_refactoring.dto.ResponseSearchPostListDto;
import com.example.leica_refactoring.entity.Post;
import com.example.leica_refactoring.entity.SearchPost;
import com.example.leica_refactoring.post.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class SearchService {

    private final SearchRepository searchRepository;
    private final PostRepository postRepository;

    @Transactional
    public ResponseSearchPostListDto searchPost(String keyword) {
        List<SearchPost> postList = searchRepository.findByContentContainingOrPostTitleContaining(keyword, keyword);

        List<ResponseSearchPostDto> collect = postList.stream().map(searchPost -> {
            String content = searchPost.getContent();
            Post post = searchPost.getPost();
            Pattern compile = Pattern.compile("/([^/]*" + keyword + "[^/]*)/");
            Matcher matcher = compile.matcher(content);
            StringBuilder matchedContent = new StringBuilder();

            while (matcher.find()) {
                matchedContent.append(matcher.group()).append(" ");
            }

            if (matchedContent.toString().trim().equals("")){
                content  = post.getContent().substring(0, Math.min(post.getContent().length(), 30));
            }else{
                 content = matchedContent.toString().trim();
            }

            return ResponseSearchPostDto.builder()
                    .id(post.getId())
                    .title(post.getTitle())
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
