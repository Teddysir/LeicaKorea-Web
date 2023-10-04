package com.example.leica_refactoring.category;

import com.example.leica_refactoring.dto.*;
import com.example.leica_refactoring.entity.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "https://localhost:3000")
public class CategoryController {

    private final CategoryService categoryService;


    @GetMapping("/find/allCategory") // 모든 부모 카테고리 반환
    public List<ResponseParentCategoryDto> getParentCategories() {
        List<ResponseParentCategoryDto> parentCategories = categoryService.getParentCategories();
        return parentCategories;
    }

    @GetMapping("/find/allCategory/{parentCategory}") // 부모카테고리 밑에있는 하위카테고리 조회
    public List<ResponseChildCategoryDto> findAllChildCategoryByParentCategory(@PathVariable String parentCategory){
        List<ResponseChildCategoryDto> allChildCategory = categoryService.findAllChildCategory(parentCategory);

        return allChildCategory;
    }

    @PostMapping("/category/parent")
    public Long createParentCategory(@RequestBody RequestParentCategoryDto parentCategory, @AuthenticationPrincipal UserDetails userDetails){
        return categoryService.createParentCategory(parentCategory, userDetails.getUsername());
    }

    @DeleteMapping("/category/parent/{parentId}")
    public ResponseEntity<String> deleteParentCategory(@PathVariable Long parentId, @AuthenticationPrincipal UserDetails userDetails){
        try {
            categoryService.deleteParentCategory(parentId,userDetails.getUsername());
            return ResponseEntity.ok("부모카테고리 삭제 성공");
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("존재하지 않는 카테고리 입니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류 발생");
        }
    }

    @PostMapping("/category/child")
    public Long createChildCategory(@RequestBody RequestChildCategoryDto childCategory, @AuthenticationPrincipal UserDetails userDetails){
        return categoryService.createChildCategory(childCategory, userDetails.getUsername());
    }

    @PutMapping("/category/{categoryId}")
    public Long updateChildCategory(@RequestBody RequestUpdateChildCategoryDto dto, @PathVariable Long categoryId ,@AuthenticationPrincipal UserDetails userDetails){
        Long update = categoryService.updateChildCategory(categoryId, dto, userDetails.getUsername());
        return update;
    }

    @DeleteMapping("/category/{categoryId}")
    public ResponseEntity<String> deleteCategory(@PathVariable Long categoryId, @AuthenticationPrincipal UserDetails userDetails){
        try {
            categoryService.deleteCategory(categoryId, userDetails.getUsername());
            return ResponseEntity.ok("카테고리 삭제 성공");
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("존재하지 않는 카테고리 입니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류 발생");
        }
    }

}
