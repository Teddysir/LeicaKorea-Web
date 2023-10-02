package com.example.leica_refactoring.category;

import com.example.leica_refactoring.dto.*;
import com.example.leica_refactoring.entity.Category;
import com.example.leica_refactoring.entity.Post;
import com.example.leica_refactoring.member.MemberRepository;
import com.example.leica_refactoring.post.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;

//    public List<ResponseParentCategoryDto> findAllParentCategory() {
//
//        List<Long> allParentCategoryIds = categoryRepository.findParentCategoryIdsByParentIsNull();
//        List<Category> allParentCategory = categoryRepository.findAllById(allParentCategoryIds);
//
//        return allParentCategory.stream()
//                .map(this::mapToResponseDto)
//                .collect(Collectors.toList());
//    }
//
//    private ResponseParentCategoryDto mapToResponseDto(Category category) {
//        return ResponseParentCategoryDto.builder()
//                .name(category.getName())
//                .parent(mapToCategoryDto(category.getParent()))
//                .build();
//    }
//
//    private CategoryDto mapToCategoryDto(Category category) {
//        if (category == null) {
//            return null;
//        }
//        return CategoryDto.builder()
//                .id(category.getId())
//                .name(category.getName())
//                .build();
//    }

    public List<ResponseParentCategoryDto> getParentCategories() {
        List<Category> parentCategories = categoryRepository.findByParentIsNull();
        return parentCategories.stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

        private ResponseParentCategoryDto mapToResponseDto(Category category) {
        return ResponseParentCategoryDto.builder()
                .name(category.getName())
                .id(category.getId())
                .build();
    }

    public List<ResponseChildCategoryDto> findAllChildCategory(String parentCategory) {
            Category category = categoryRepository.findByName(parentCategory);


        List<ResponseChildCategoryDto> childCategoryDtos = category.getChild().stream()
                .map(childCategory -> {
                    List<Post> numbersOfPost = postRepository.findByChildCategory(childCategory);
                    // ResponseChildCategoryDto.builder()를 호출하고 필드를 적절히 매핑한 후 객체를 생성하고 반환합니다.
                    return ResponseChildCategoryDto.builder()
                            .id(childCategory.getId())
                            .size(numbersOfPost.size())
                            .childName(childCategory.getName())
                            .build();
                })
                .collect(Collectors.toList());

        return childCategoryDtos;

    }
    public Long createParentCategory(RequestParentCategoryDto parentCategory) {
        String parentName = parentCategory.getParentName();
        Category category = categoryRepository.findByName(parentName);
        if(category != null){
            throw new CategoryAlreadyExistsException(parentName);
        }else {
            Category category1 = Category.builder()
                    .name(parentName)
                    .parent(null)
                    .build();

            Category save = categoryRepository.save(category1);
            return save.getId();
        }

    }

    public Long createChildCategory(RequestChildCategoryDto childCategory) {
        Category parentCategory = categoryRepository.findByName(childCategory.getParentName());
        if(parentCategory == null){
            throw new ParentCategoryNotFoundException(childCategory.getParentName());
        }else{
            String childName = childCategory.getChildName();
            Category category = Category.builder()
                    .name(childName)
                    .parent(parentCategory)
                    .build();

            Category save = categoryRepository.save(category);
            return save.getId();
        }


    }

    public void deleteCategory(Long categoryId) {
        Optional<Category> category = categoryRepository.findById(categoryId);
        category.ifPresentOrElse(c -> categoryRepository.deleteById(categoryId),
                ()-> {
                    throw new NoSuchElementException("존재하지 않는 카테고리 입니다.");
                    });
    }

    public Long updateChildCategory(Long categoryId, RequestUpdateChildCategoryDto dto) {

        Optional<Category> originCategory = categoryRepository.findById(categoryId);
        if(!originCategory.isPresent()) {
            throw new CategoryIsNotExists("존재하지 않는 카테고리입니다.");
        }

        Category childCategoryId = originCategory.get();
        childCategoryId.setName(dto.getChildName());
        categoryRepository.save(childCategoryId);

        return childCategoryId.getId();

    }


}
