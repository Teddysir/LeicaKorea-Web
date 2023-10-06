package com.example.leica_refactoring.post;

import com.example.leica_refactoring.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "https://localhost:3000")
@Tag(name = "Post Controller", description = "Post API")
public class PostController {

    private final PostService postService;
    private final PostRepository postRepository;

    @Operation(summary = "모든 게시물 조회")
    @GetMapping("/post")
    public PaginationDto post(Pageable pageable){
        PaginationDto all = postService.findAll(pageable);
        return all;
    }

    // 카테고리별 게시물 조회(부모 카테고리 기준)
    @Operation(summary = "카테고리별 게시물 조회(부모 카테고리 기준")
    @GetMapping("/post/{parentCategory}")
    public PaginationDto findAllPostByParentCategory(@PathVariable String parentCategory, Pageable pageable){
        PaginationDto allPostByParentCategory = postService.findAllPostByParentCategory(parentCategory, pageable);
        return allPostByParentCategory;
    }

    // 카테고리별 게시물 조회(자식 카테고리 기준)
    @GetMapping("/post/{parentCategory}/{childCategory}")
    @Operation(summary = "카테고리별 게시물 조회(자식 카테고리 기준)")
    public PaginationDto findAllPostByChildCategory(@PathVariable String parentCategory, @PathVariable String childCategory, Pageable pageable){
        PaginationDto allPostByChildCategory = postService.findAllPostByChildCategory(parentCategory, childCategory, pageable);
        return allPostByChildCategory;
    }

    // 게시물 생성(ADMIN만 가능)
    @PostMapping("/post")
    @Operation(summary = "게시물 생성 (유저권한 필요)")
    public ResponseEntity<String> createPost(@RequestBody RequestPostWithSearchableDto requestPostWithSearchableDto,
                                             @AuthenticationPrincipal UserDetails userDetails){

        Long save = postService.save(requestPostWithSearchableDto, userDetails.getUsername());

        return ResponseEntity.ok().body("{\"postId\": " + save + "}");
    }

    @GetMapping("/post/find/{id}")
    @Operation(summary = "하나의 게시물 조회")
    public ResponsePostOneDto findPost(@PathVariable Long id){

        ResponsePostOneDto save = postService.showPost(id);

        return save;
    }

    // 자기 자신만 가능!
    @PutMapping("/post/{id}")
    @Operation(summary = "게시물 업데이트 (유저권한 필요)")
    public Long updatePost(@RequestBody RequestPostWithSearchableDto requestPostWithSearchableDto, @PathVariable Long id,
                           @AuthenticationPrincipal UserDetails userDetails){

        Long update = postService.update(id, requestPostWithSearchableDto, userDetails.getUsername());
        return update;
    }

    // 자기 자신만 가능
    @DeleteMapping("/post/{id}")
    @Operation(summary = "게시물 삭제 (유저권한 필요)")
    public void deletePost(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails){
        postService.delete(id, userDetails.getUsername());
    }

}
