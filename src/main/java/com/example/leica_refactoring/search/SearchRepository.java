package com.example.leica_refactoring.search;

import com.example.leica_refactoring.entity.Post;
import com.example.leica_refactoring.entity.SearchPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface SearchRepository extends JpaRepository<SearchPost, Long> {

    List<SearchPost> findBySearchContentContainingOrPostTitleContaining(String keyword, String keyword2);

    SearchPost findByPost_Id(Long id);
}
