package com.example.leica_refactoring.post;

import com.example.leica_refactoring.dto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "https://localhost:3000")
public class PostController {

    private final PostService postService;
    private final PostRepository postRepository;

    // 전체 게시물 조회
    @GetMapping("/")
    public ResponsePostListDto post(Pageable pageable){
        ResponsePostListDto all = postService.findAll(pageable);
        return all;
    }

    // 카테고리별 게시물 조회(부모 카테고리 기준)
    @GetMapping("/find/post/{parentCategory}")
    public ResponsePostListDto findAllPostByParentCategory(@PathVariable String parentCategory, Pageable pageable){
        ResponsePostListDto allPostByParentCategory = postService.findAllPostByParentCategory(parentCategory, pageable);
        return allPostByParentCategory;
    }

    // 카테고리별 게시물 조회(자식 카테고리 기준)
    @GetMapping("/find/post/{parentCategory}/{childCategory}")
    public ResponsePostListDto findAllPostByChildCategory(@PathVariable String parentCategory, @PathVariable String childCategory, Pageable pageable){
        ResponsePostListDto allPostByChildCategory = postService.findAllPostByChildCategory(parentCategory, childCategory, pageable);
        return allPostByChildCategory;
    }




    // 게시물 생성(ADMIN만 가능)
    @PostMapping("/post")
    public ResponseEntity<String> createPost(@RequestBody RequestPostWithSearchableDto requestPostWithSearchableDto,
                                             @AuthenticationPrincipal UserDetails userDetails){

        Long save = postService.save(requestPostWithSearchableDto, userDetails.getUsername());

        return ResponseEntity.ok().body("{\"postId\": " + save + "}");
    }

    @GetMapping("/find/{id}")
    public ResponsePostOneDto createPost(@PathVariable Long id){

        ResponsePostOneDto save = postService.showPost(id);

        return save;
    }

    // 자기 자신만 가능!
    @PutMapping("/post/{id}")
    public Long updatePost(@RequestBody RequestPostWithSearchableDto requestPostWithSearchableDto, @PathVariable Long id,
                           @AuthenticationPrincipal UserDetails userDetails){

        Long update = postService.update(id, requestPostWithSearchableDto, userDetails.getUsername());
        return update;
    }

    // 자기 자신만 가능

    @DeleteMapping("/post/{id}")
    public void deletePost(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails){
        postService.delete(id, userDetails.getUsername());
    }

}
