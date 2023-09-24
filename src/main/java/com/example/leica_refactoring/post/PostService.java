package com.example.leica_refactoring.post;

import com.example.leica_refactoring.category.CategoryRepository;
import com.example.leica_refactoring.dto.*;
import com.example.leica_refactoring.entity.Category;
import com.example.leica_refactoring.entity.Member;
import com.example.leica_refactoring.entity.Post;
import com.example.leica_refactoring.entity.SearchPost;
import com.example.leica_refactoring.member.MemberRepository;
import com.example.leica_refactoring.search.SearchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final CategoryRepository categoryRepository;
    private final SearchRepository searchRepository;


    // 게시물 생성
    public Long save(RequestPostWithSearchableDto requestPostDto, String memberId) {
        Member member = memberRepository.findByMemberId(memberId);
        if(member == null){
            throw new UsernameNotFoundException("존재하는 사용자가 없습니다.");
        }else{
            String content = requestPostDto.getContent();
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
                    .content(content)
                    .build();
            SearchPost save2 = searchRepository.save(searchPost);

            return save.getId();
        }
    }


    // 전체 게시물 반환
    public ResponsePostListDto findAll() {
        List<Post> all = postRepository.findAll();
        if (all.isEmpty()) {
            return ResponsePostListDto.builder()
                    .size(0L)
                    .childList(Collections.emptyList())
                    .build();
        }else{
            int size = all.size();

            List<ResponsePostDto> collect = all.stream()
                    .filter(Objects::nonNull)
                    .map(PostService::getBuild)
                    .collect(Collectors.toList());

            return ResponsePostListDto.builder()
                    .size((long) size)
                    .childList(collect)
                    .build();}

    }


    // 부모 카테고리안에 존재하는 모든 게시물 반환
    public ResponsePostListDto findAllPostByParentCategory(String parentName) {
        Category category = categoryRepository.findByName(parentName);
        if(category == null){
            return ResponsePostListDto.builder()
                    .size(0L) // 게시물 수를 0으로 설정
                    .childList(Collections.emptyList()) // 빈 리스트 설정
                    .build();
        }else{
            Long totalPostCount = (long) category.getChild().stream()
                    .flatMap(child -> child.getPosts().stream()).mapToInt(post -> 1).sum(); // 각 게시물에 대해 1을 더함

            List<ResponsePostDto> postDtos = category.getChild().stream()
                    .flatMap(child -> child.getPosts().stream()
                            .map(PostService::getBuild
                            )
                    )
                    .collect(Collectors.toList());

            return ResponsePostListDto.builder()
                    .size(totalPostCount)
                    .childList(postDtos)
                    .build();
        }
    }

    // 자식 카테고리 안에있는 모든 게시물 반환
    public ResponsePostListDto findAllPostByChildCategory(String parentName, String childName) {
        List<Category> childCategories = categoryRepository.findAllByName(childName);


        Category selectedChildCategory = null;
        for (Category category : childCategories) {
            if (category.getParent().getName().equals(parentName)) {
                selectedChildCategory = category;
                break;
            }
        }

        if (selectedChildCategory == null) {
            return ResponsePostListDto.builder()
                    .size(0L)
                    .childList(Collections.emptyList())
                    .build();
        } else {
            Long totalPostCount = (long) selectedChildCategory.getPosts().size();

            List<ResponsePostDto> postDtos = selectedChildCategory.getPosts().stream()
                    .map(PostService::getBuild
                    )
                    .collect(Collectors.toList());

            return ResponsePostListDto.builder()
                    .size(totalPostCount)
                    .childList(postDtos)
                    .build();
        }
    }



    // 내용 업데이트
    public Long update(Long id, RequestPostDto requestPostDto, String username) {
        Member member = validateMemberAndPost(id, username);
        Post originPost = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("게시물이 존재하지 않습니다."));

        // 게시물 내용 업데이트
        originPost.setTitle(requestPostDto.getTitle());
        originPost.setContent(requestPostDto.getContent());
        originPost.setThumbnail(requestPostDto.getThumbnail());
        originPost.setMember(member);

        // 업데이트된 게시물 저장
        Post updatedPost = postRepository.save(originPost);
        return updatedPost.getId();

    }



    public void delete(Long id, String username) {
        validateMemberAndPost(id, username);
        postRepository.deleteById(id);

    }


    public ResponsePostOneDto showPost(Long id) {
        Optional<Post> postOptional = postRepository.findById(id);
        Post post = postOptional.orElseThrow(() -> new NoSuchElementException("게시물이 존재하지 않습니다."));

        return getOneBuild(post);
    }



    private Member validateMemberAndPost(Long id, String username) {
        Member member = memberRepository.findByMemberId(username);
        Optional<Post> post = postRepository.findById(id);
        if (member == null){
            throw new UsernameNotFoundException("사용자가 존재하지 않습니다.");

        }
        if(!Objects.equals(post.get().getMember().getMemberId(), username)){
            throw new AuthorOnlyAccessException();
        }
        return member;
    }


    private static ResponsePostDto getBuild(Post post) {
        if (post != null) {
            return ResponsePostDto.builder()
                    .id(post.getId())
                    .title(post.getTitle())
                    .subTitle(post.getSubTitle())
                    .thumbnail(post.getThumbnail())
                    .create_at(post.getCreate_at())
                    .modified_at(post.getModified_at())
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
                    .writer(post.getMember() != null ? post.getMember().getMemberId() : null)
                    .category(post.getChildCategory() != null ? post.getChildCategory().getName() : null)
                    .build();
        } else {
            return null;
        }
    }


}
