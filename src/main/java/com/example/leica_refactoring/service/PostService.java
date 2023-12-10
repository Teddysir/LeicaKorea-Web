package com.example.leica_refactoring.service;

import com.example.leica_refactoring.error.exception.requestError.UnAuthorizedException;
import com.example.leica_refactoring.error.security.ErrorCode;
import com.example.leica_refactoring.repository.CategoryRepository;
import com.example.leica_refactoring.dto.post.*;
import com.example.leica_refactoring.dto.search.RequestPostWithSearchableDto;
import com.example.leica_refactoring.entity.Category;
import com.example.leica_refactoring.entity.Member;
import com.example.leica_refactoring.entity.Post;
import com.example.leica_refactoring.entity.SearchPost;
import com.example.leica_refactoring.repository.MemberRepository;
import com.example.leica_refactoring.error.exception.AuthorOnlyAccessException;
import com.example.leica_refactoring.repository.PostRepository;
import com.example.leica_refactoring.enums.UserRole;
import com.example.leica_refactoring.repository.SearchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final MemberService memberService;
    private final CategoryRepository categoryRepository;
    private final SearchRepository searchRepository;


    // 게시물 생성
    public Long save(RequestPostWithSearchableDto requestPostDto, HttpServletRequest request) {
        Member member = memberService.findMemberByToken(request);
        if (member.getUserRole() != UserRole.ADMIN) {
            throw new UnAuthorizedException("유저 권한이 없습니다.", ErrorCode.ACCESS_DENIED_EXCEPTION);
        } else {
            String content = requestPostDto.getSearchContent();
            RequestPostDto postDto = requestPostDto.getPost();

            Category category = categoryRepository.findByName(postDto.getParentName());

            Category childCategory = null;

            for (Category category1 : category.getChild()) {
                if (category1.getName().equals(postDto.getChildName())) {
                    childCategory = category1;
                    break;
                }
            }

            Post post = Post.builder()
                    .title(postDto.getTitle())
                    .content(postDto.getContent())
                    .subTitle(postDto.getSubTitle())
                    .thumbnail(postDto.getThumbnail())
                    .childCategory(childCategory)
                    .member(member)
                    .build();
            Post save = postRepository.save(post);


            SearchPost searchPost = SearchPost.builder()
                    .post(save)
                    .searchContent(content)
                    .build();
            SearchPost save2 = searchRepository.save(searchPost);

            return save.getId();
        }
    }


    // 전체 게시물 반환
    public ResponsePostListDto findAll() {
        List<Post> all = postRepository.findTop5ByOrderByCreatedAtDesc();
        if (all.isEmpty()) {
            return ResponsePostListDto.builder()
                    .size(0L)
                    .childList(Collections.emptyList())
                    .build();
        } else {
            int size = all.size();

            List<ResponsePostDto> collect = all.stream()
                    .filter(Objects::nonNull)
                    .map(this::getBuild)
                    .collect(Collectors.toList());

            return ResponsePostListDto.builder()
                    .size((long) size)
                    .childList(collect)
                    .build();
        }

    }


    // 부모 카테고리안에 존재하는 모든 게시물 반환
    public PaginationDto findAllPostByParentCategory(String parentName, Pageable pageable) {
        Category category = categoryRepository.findByName(parentName);
        if (category == null) {
            return getPaginationDto(0L, true, 0L, Collections.emptyList());

        } else {
            List<Post> allPosts = category.getChild().stream()
                    .flatMap(child -> child.getPosts().stream())
                    .sorted(Comparator.comparing(Post::getCreatedAt).reversed())
                    .collect(Collectors.toList());

            // 페이지네이션을 적용합니다.
            int pageSize = pageable.getPageSize();
            int currentPage = pageable.getPageNumber();
            int startItem = currentPage * pageSize;

            List<ResponsePostDto> postDtos;

            if (startItem < allPosts.size()) {
                int endItem = Math.min(startItem + pageSize, allPosts.size());
                List<Post> pageContent = allPosts.subList(startItem, endItem);

                postDtos = pageContent.stream()
                        .map(this::getBuild)
                        .collect(Collectors.toList());

                long totalPages = (long) Math.ceil((double) allPosts.size() / (double) pageSize);
                boolean isLastPage = !pageable.isPaged() || currentPage >= totalPages - 1;

                return getPaginationDto(totalPages, isLastPage, (long) allPosts.size(), postDtos);
            } else {
                return getPaginationDto(0L, true, 0L, Collections.emptyList());

            }
        }
    }

    // 자식 카테고리 안에있는 모든 게시물 반환
    public PaginationDto findAllPostByChildCategory(String parentName, String childName, Pageable pageable) {
        List<Category> childCategories = categoryRepository.findAllByName(childName);

        Category selectedChildCategory = null;
        for (Category category : childCategories) {
            if (category.getParent().getName().equals(parentName)) {
                selectedChildCategory = category;
                break;
            }
        }

        if (selectedChildCategory == null) {
            return getPaginationDto(0L, true, 0L, Collections.emptyList());

        } else {
            List<Post> allPosts = selectedChildCategory.getPosts()
                    .stream()
                    .sorted(Comparator.comparing(Post::getCreatedAt).reversed()) // 최신 순으로 정렬
                    .collect(Collectors.toList());

            int pageSize = pageable.getPageSize();
            int currentPage = pageable.getPageNumber();
            int startItem = currentPage * pageSize;

            List<ResponsePostDto> postDtos;

            if (startItem < allPosts.size()) {
                int endItem = Math.min(startItem + pageSize, allPosts.size());
                postDtos = allPosts.subList(startItem, endItem).stream()
                        .map(this::getBuild)
                        .collect(Collectors.toList());
            } else {
                postDtos = Collections.emptyList();
            }

            long totalPages = (long) Math.ceil((double) allPosts.size() / (double) pageSize);
            boolean isLastPage = !pageable.isPaged() || currentPage >= totalPages - 1;

            return PaginationDto.builder()
                    .lastPage(isLastPage)
                    .totalPage(totalPages)
                    .totalElement((long) allPosts.size())
                    .childList(postDtos)
                    .build();
        }
    }


    // 내용 업데이트
    public Long update(Long id, RequestPostWithSearchableDto requestPostDto, HttpServletRequest request) {
        Member member = validateMemberAndPost(id, request);

        Post originPost = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("게시물이 존재하지 않습니다."));

        String content = requestPostDto.getSearchContent();
        RequestPostDto post = requestPostDto.getPost();

        SearchPost searchPost = searchRepository.findByPost_Id(id);

        searchPost.setSearchContent(content);

        // 게시물 내용 업데이트
        originPost.setTitle(post.getTitle());
        originPost.setSubTitle(post.getSubTitle());
        originPost.setContent(post.getContent());
        originPost.setThumbnail(post.getThumbnail());
        originPost.setMember(member);

        // 업데이트된 게시물 저장
        Post updatedPost = postRepository.save(originPost);
        SearchPost save = searchRepository.save(searchPost);
        return updatedPost.getId();

    }


    public void delete(Long id, HttpServletRequest request) {
        validateMemberAndPost(id, request);
        postRepository.deleteById(id);

    }

    public ResponsePostOneDto showPost(Long id) {
        Optional<Post> postOptional = postRepository.findById(id);
        Post post = postOptional.orElseThrow(() -> new NoSuchElementException("게시물이 존재하지 않습니다."));

        return getOneBuild(post);
    }

    private Member validateMemberAndPost(Long id, HttpServletRequest request) {
        Member member = memberService.findMemberByToken(request);

        Optional<Post> post = postRepository.findById(id);
        if (member.getUserRole() != UserRole.ADMIN) {
            throw new UnAuthorizedException("유저 권한이 없습니다.",ErrorCode.ACCESS_DENIED_EXCEPTION);
        }

        if (!Objects.equals(post.get().getMember().getMemberId(), member.getMemberId())) {
            throw new AuthorOnlyAccessException();
        }
        return member;
    }

    private ResponsePostDto getBuild(Post post) {
        if (post != null) {
            SearchPost byPostId = searchRepository.findByPost_Id(post.getId());
            String content = byPostId.getSearchContent();
            content = content.replace("/", "");
            content = content.substring(0, Math.min(content.length(), 30));

            return ResponsePostDto.builder()
                    .id(post.getId())
                    .title(post.getTitle())
                    .content(content)
                    .subTitle(post.getSubTitle())
                    .createdAt(post.getCreatedAt())
                    .modified_at(post.getModified_at())
                    .thumbnail(post.getThumbnail())
                    .category(post.getChildCategory() != null ? post.getChildCategory().getName() : null)
                    .build();
        } else {
            return null;
        }
    }

    private static ResponsePostOneDto getOneBuild(Post post) {
        if (post != null) {
            return ResponsePostOneDto.builder()
                    .id(post.getId())
                    .title(post.getTitle())
                    .content(post.getContent())
                    .subTitle(post.getSubTitle())
                    .thumbnail(post.getThumbnail())
                    .createdAt(post.getCreatedAt())
                    .modified_at(post.getModified_at())
                    .parentCategory(post.getChildCategory() != null ? post.getChildCategory().getParent().getName() : null)
                    .category(post.getChildCategory() != null ? post.getChildCategory().getName() : null)
                    .build();
        } else {
            return null;
        }
    }

    private static PaginationDto getPaginationDto(Long pageSize, boolean isLast, Long size, List childList) {
        return PaginationDto.builder()
                .totalPage(pageSize)
                .lastPage(isLast)
                .totalElement(size)
                .childList(childList)
                .build();
    }

}
