package com.example.leica_refactoring.category;

import com.example.leica_refactoring.dto.*;
import com.example.leica_refactoring.entity.Category;
import com.example.leica_refactoring.entity.Member;
import com.example.leica_refactoring.entity.Post;
import com.example.leica_refactoring.member.MemberRepository;
import com.example.leica_refactoring.post.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
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

    public PaginationCategoryDto findAllChildCategory(String parentCategory, Pageable pageable) {
        Category category = categoryRepository.findByName(parentCategory);

        List<ResponseChildCategoryDto> childCategoryDtos = category.getChild().stream()
                .map(childCategory -> {
                    List<Post> numbersOfPost = postRepository.findByChildCategory(childCategory);
                    return ResponseChildCategoryDto.builder()
                            .id(childCategory.getId())
                            .size(numbersOfPost.size())
                            .childName(childCategory.getName())
                            .build();
                })
                .collect(Collectors.toList());

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), childCategoryDtos.size());



        long totalPage = (long) Math.ceil((double) childCategoryDtos.size() / pageable.getPageSize());
        boolean isLastPage = !pageable.isPaged() || pageable.getPageNumber() >= totalPage - 1;
        long totalElement = (long) childCategoryDtos.size();

        if (start >= childCategoryDtos.size() || start >= end) {
            return new PaginationCategoryDto(totalPage, true, totalElement, Collections.emptyList());
        }

        List<ResponseChildCategoryDto> paginatedChildCategoryDtos = childCategoryDtos.subList(start, end);

        return new PaginationCategoryDto(totalPage, isLastPage, totalElement, paginatedChildCategoryDtos);
    }
    public Long createParentCategory(RequestParentCategoryDto parentCategory, String memberId) {
        Member member = memberRepository.findByMemberId(memberId);

        if(member == null) {
            throw new UsernameNotFoundException("존재하는 사용자가 없습니다.");
        }else{
            Long parentCategoryCount = categoryRepository.countByParentIsNull();
            Long maxParentCategoryCount = Long.valueOf(8);

            if (parentCategoryCount >= maxParentCategoryCount) {
                throw new ParentCategoryMaxException(maxParentCategoryCount);
            }

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
    }

    public Long createChildCategory(RequestChildCategoryDto childCategory, String memberId) {
        Member member = memberRepository.findByMemberId(memberId);
        if(member == null) {
            throw new UsernameNotFoundException("존재하는 사용자가 없습니다.");
        }else{
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
    }

    public void deleteParentCategory(Long parentId, String memberId) {
        Member member = memberRepository.findByMemberId(memberId);

        if (member == null) {
            throw new UsernameNotFoundException("존재하는 사용자가 없습니다.");
        } else {
            Optional<Category> parentCategory = categoryRepository.findByIdAndParentIsNull(parentId);

            parentCategory.ifPresentOrElse(
                    c -> {
                        if (c.getChild().isEmpty()) {
                            categoryRepository.deleteById(parentId);
                        } else {
                            throw new IllegalStateException("자식 카테고리가 존재하여 삭제할 수 없습니다.");
                        }
                    },
                    () -> {
                        throw new NoSuchElementException("존재하지 않는 부모 카테고리입니다.");
                    });
        }
    }


    public void deleteCategory(Long categoryId, String memberId) {
        Member member = memberRepository.findByMemberId(memberId);

        if(member == null) {
            throw new UsernameNotFoundException("존재하는 사용자가 없습니다.");
        }else{
            Optional<Category> category = categoryRepository.findById(categoryId);
            category.ifPresentOrElse(c -> categoryRepository.deleteById(categoryId),
                    ()-> {
                        throw new NoSuchElementException("존재하지 않는 카테고리 입니다.");
                    });
        }

    }

    public Long updateChildCategory(Long categoryId, RequestUpdateChildCategoryDto dto, String memberId) {
        Member member = memberRepository.findByMemberId(memberId);
        if(member == null) {
            throw new UsernameNotFoundException("존재하는 사용자가 없습니다.");
        }else{
            Optional<Category> originCategory = categoryRepository.findById(categoryId);
            if(!originCategory.isPresent()) {
                throw new CategoryIsNotExists("존재하지 않는 카테고리입니다.");
            }

            Category CategoryId = originCategory.get();
            CategoryId.setName(dto.getCategoryName());
            categoryRepository.save(CategoryId);

            return CategoryId.getId();

        }

    }

}
