package com.example.leica_refactoring.service;

import com.example.leica_refactoring.dto.category.*;
import com.example.leica_refactoring.entity.Category;
import com.example.leica_refactoring.entity.Member;
import com.example.leica_refactoring.entity.Post;
import com.example.leica_refactoring.error.exception.CategoryAlreadyExistsException;
import com.example.leica_refactoring.error.exception.CategoryIsNotExists;
import com.example.leica_refactoring.error.exception.ParentCategoryMaxException;
import com.example.leica_refactoring.error.exception.ParentCategoryNotFoundException;
import com.example.leica_refactoring.error.exception.requestError.BadRequestException;
import com.example.leica_refactoring.error.exception.requestError.UnAuthorizedException;
import com.example.leica_refactoring.error.security.ErrorCode;
import com.example.leica_refactoring.repository.CategoryRepository;
import com.example.leica_refactoring.repository.MemberRepository;
import com.example.leica_refactoring.enums.UserRole;
import com.example.leica_refactoring.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final PostRepository postRepository;
    private final MemberService memberService;

    public List<ResponseParentCategoryDto> getParentCategories() {
        List<Category> parentCategories = categoryRepository.findByParentIsNull();
        return parentCategories.stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    private ResponseParentCategoryDto mapToResponseDto(Category category) {
        return ResponseParentCategoryDto.builder()
                .parentName(category.getName())
                .build();
    }

    public List<ResponseChildCategoryDto> findAllChildCategory(Long parentCategoryId) {

        Category categoryId = categoryRepository.findCategoryById(parentCategoryId);
        Category categoryName = categoryRepository.findByName(categoryId.getName());

        if(categoryId == null) {
            throw new BadRequestException("400",ErrorCode.RUNTIME_EXCEPTION);
        }

        List<ResponseChildCategoryDto> childCategoryDtos = categoryName.getChild().stream()
                .map(childCategory -> {
//                    List<Post> numbersOfPost = postRepository.findByChildCategory(childCategory);
                    return ResponseChildCategoryDto.builder()
                            .childId(childCategory.getId())
                            .childName(childCategory.getName())
                            .build();
                })
                .collect(Collectors.toList());

        return childCategoryDtos;

    }


    public Long createParentCategory(RequestParentCategoryDto parentCategory, HttpServletRequest request) {
        Member member = memberService.findMemberByToken(request);

        if (member.getUserRole() != UserRole.ADMIN) {
            throw new UnAuthorizedException("유저 권한이 없습니다.", ErrorCode.ACCESS_DENIED_EXCEPTION);
        } else {
            Long parentCategoryCount = categoryRepository.countByParentIsNull();
            Long maxParentCategoryCount = Long.valueOf(8);

            if (parentCategoryCount >= maxParentCategoryCount) {
                throw new ParentCategoryMaxException(maxParentCategoryCount);
            }

            String parentName = parentCategory.getParentName();
            Category category = categoryRepository.findByName(parentName);

            if (category != null) {
                throw new CategoryAlreadyExistsException(parentName);
            } else {
                Category category1 = Category.builder()
                        .name(parentName)
                        .parent(null)
                        .build();

                Category save = categoryRepository.save(category1);
                return save.getId();
            }
        }
    }

    public Long createChildCategory(RequestChildCategoryDto childCategory, HttpServletRequest request) {
        Member member = memberService.findMemberByToken(request);

        if (member.getUserRole() != UserRole.ADMIN) {
            throw new UnAuthorizedException("유저 권한이 없습니다.", ErrorCode.ACCESS_DENIED_EXCEPTION); // 나중에 오류처리 모두 ErrorCode 코드 추가해서 적용해주기
        } else {
            Category parentCategory = categoryRepository.findCategoryById(childCategory.getParentId());
            if (parentCategory == null) {
                throw new BadRequestException("401",ErrorCode.RUNTIME_EXCEPTION);
            } else {
                String childName = childCategory.getChildName();
                Category category = categoryRepository.findByName(childName);
                if (category != null) {
                    throw new CategoryAlreadyExistsException(childName);
                } else {
                    Category category1 = Category.builder()
                            .name(childName)
                            .parent(parentCategory)
                            .build();

                    Category save = categoryRepository.save(category1);
                    return save.getId();
                }
            }
        }
    }

    public void deleteParentCategory(Long parentId, HttpServletRequest request) {
        Member member = memberService.findMemberByToken(request);

        if (member.getUserRole() != UserRole.ADMIN) {
            throw new UnAuthorizedException("유저 권한이 없습니다.", ErrorCode.ACCESS_DENIED_EXCEPTION);
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


    public void deleteCategory(Long categoryId, HttpServletRequest request) {
        Member member = memberService.findMemberByToken(request);

        if (member.getUserRole() != UserRole.ADMIN) {
            throw new UnAuthorizedException("유저 권한이 없습니다.", ErrorCode.ACCESS_DENIED_EXCEPTION);
        } else {
            Optional<Category> category = categoryRepository.findById(categoryId);
            category.ifPresentOrElse(c -> categoryRepository.deleteById(categoryId),
                    () -> {
                        throw new NoSuchElementException("존재하지 않는 카테고리 입니다.");
                    });
        }

    }

    public Long updateChildCategory(Long categoryId, RequestUpdateChildCategoryDto dto, HttpServletRequest request) {
        Member member = memberService.findMemberByToken(request);

        if (member.getUserRole() != UserRole.ADMIN) {
            throw new UnAuthorizedException("유저 권한이 없습니다.", ErrorCode.ACCESS_DENIED_EXCEPTION);
        } else {
            Optional<Category> originCategory = categoryRepository.findById(categoryId);
            if (!originCategory.isPresent()) {
                throw new CategoryIsNotExists("존재하지 않는 카테고리입니다.");
            }

            Category CategoryId = originCategory.get();
            CategoryId.setName(dto.getCategoryName());
            categoryRepository.save(CategoryId);

            return CategoryId.getId();

        }

    }

}
