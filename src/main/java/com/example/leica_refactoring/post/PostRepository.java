package com.example.leica_refactoring.post;

import com.example.leica_refactoring.entity.Category;
import com.example.leica_refactoring.entity.Member;
import com.example.leica_refactoring.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllByOrderByCreatedAtDesc();

    Page<Post> findAllByOrderByCreatedAtDesc(Pageable pageable);
    List<Post> findByMemberMemberId(String username);
    List<Post> findByChildCategory(Category category);

}
