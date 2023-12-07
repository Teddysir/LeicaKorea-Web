package com.example.leica_refactoring.controller;

import com.example.leica_refactoring.service.CategoryService;
import com.example.leica_refactoring.dto.category.*;
import com.example.leica_refactoring.jwt.JwtTokenProvider;
import com.example.leica_refactoring.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = {"https://localhost:3000", "https://www.nts-microscope.com"})
@Tag(name = "Category Controller", description = "Category API")
public class CategoryController {

    private final CategoryService categoryService;
    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;

    @Operation(summary = "모든 부모 카테고리 반환")
    @GetMapping("/category/parent") // 모든 부모 카테고리 반환
    public List<ResponseParentCategoryDto> getParentCategories() {
        List<ResponseParentCategoryDto> parentCategories = categoryService.getParentCategories();
        return parentCategories;
    }

    @Operation(summary = "부모 카테고리 밑에있는 하위 카테고리 조회")
    @GetMapping("/category/{parentCategory}") // 부모카테고리 밑에있는 하위카테고리 조회
    public List<ResponseChildCategoryDto> findAllChildCategoryByParentCategory(
            @PathVariable String parentCategory) {
        List<ResponseChildCategoryDto> allChildCategory = categoryService.findAllChildCategory(parentCategory);

        return allChildCategory;
    }

    @Operation(summary = "부모 카테고리 생성 (유저권한 필요)")
    @PostMapping("/category/parent")
    public Long createParentCategory(@RequestBody RequestParentCategoryDto parentCategory, HttpServletRequest request) {
        return categoryService.createParentCategory(parentCategory, request);

    }

    @Operation(summary = "자식 카테고리 생성 (유저권한 필요)")
    @PostMapping("/category/child")
    public Long createChildCategory(@RequestBody RequestChildCategoryDto childCategory, HttpServletRequest request) {
        return categoryService.createChildCategory(childCategory, request);
    }

    @Operation(summary = "부모, 자식 카테고리 수정 (유저권한 필요)")
    // 부모, 자식 카테고리 모두 같이 사용 가능
    @PutMapping("/category/{categoryId}")
    public Long updateChildCategory(@RequestBody RequestUpdateChildCategoryDto dto, @PathVariable Long categoryId, HttpServletRequest request) {
        Long update = categoryService.updateChildCategory(categoryId, dto, request);
        return update;
    }

    @Operation(summary = "부모 카테고리 삭제 (유저권한 필요)")
    @DeleteMapping("/category/{parentId}")
    public ResponseEntity<String> deleteParentCategory(@PathVariable Long parentId, HttpServletRequest request) {
        try {
            categoryService.deleteParentCategory(parentId, request);
            return ResponseEntity.ok("부모카테고리 삭제 성공");
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("존재하지 않는 카테고리 입니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류 발생");
        }
    }


    @Operation(summary = "자식 카테고리 삭제 (유저권한 필요)")
    @DeleteMapping("/category/child/{categoryId}")
    public ResponseEntity<String> deleteCategory(@PathVariable Long categoryId, HttpServletRequest request) {
        try {
            categoryService.deleteCategory(categoryId, request);
            return ResponseEntity.ok("카테고리 삭제 성공");
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("존재하지 않는 카테고리 입니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류 발생");
        }
    }

}
