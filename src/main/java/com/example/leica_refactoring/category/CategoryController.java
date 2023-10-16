package com.example.leica_refactoring.category;

import com.example.leica_refactoring.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = {"https://localhost:3000","https://www.nts-microscope.com"})
@Tag(name = "Category Controller", description = "Category API")
public class CategoryController {

    private final CategoryService categoryService;

    @Operation(summary = "모든 부모 카테고리 반환")
    @GetMapping("/category/parent") // 모든 부모 카테고리 반환
    public List<ResponseParentCategoryDto> getParentCategories() {
        List<ResponseParentCategoryDto> parentCategories = categoryService.getParentCategories();
        return parentCategories;
    }

    @Operation(summary = "부모 카테고리 밑에있는 하위 카테고리 조회")
    @GetMapping("/category/{parentCategory}") // 부모카테고리 밑에있는 하위카테고리 조회
    public List<ResponseChildCategoryDto> findAllChildCategoryByParentCategory(
            @PathVariable String parentCategory){
        List<ResponseChildCategoryDto> allChildCategory = categoryService.findAllChildCategory(parentCategory);

        return allChildCategory;
    }

    @Operation(summary = "부모 카테고리 생성 (유저권한 필요)")
    @PostMapping("/category/parent")
    public Long createParentCategory(@RequestBody RequestParentCategoryDto parentCategory, @AuthenticationPrincipal UserDetails userDetails){
        return categoryService.createParentCategory(parentCategory, userDetails.getUsername());
    }

    @Operation(summary = "자식 카테고리 생성 (유저권한 필요)")
    @PostMapping("/category/child")
    public Long createChildCategory(@RequestBody RequestChildCategoryDto childCategory, @AuthenticationPrincipal UserDetails userDetails){
        return categoryService.createChildCategory(childCategory, userDetails.getUsername());
    }

    @Operation(summary = "부모, 자식 카테고리 수정 (유저권한 필요)")
    // 부모, 자식 카테고리 모두 같이 사용 가능
    @PutMapping("/category/{categoryId}")
    public Long updateChildCategory(@RequestBody RequestUpdateChildCategoryDto dto, @PathVariable Long categoryId ,@AuthenticationPrincipal UserDetails userDetails){
        Long update = categoryService.updateChildCategory(categoryId, dto, userDetails.getUsername());
        return update;
    }

    @Operation(summary = "부모 카테고리 삭제 (유저권한 필요)")
    @DeleteMapping("/category/{parentId}")
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


    @Operation(summary = "자식 카테고리 삭제 (유저권한 필요)")
    @DeleteMapping("/category/child/{categoryId}")
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
