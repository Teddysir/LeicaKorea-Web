package com.example.leica_refactoring.repository;

import com.example.leica_refactoring.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    Category findByName(String categoryName);

    Page<Category> findByName(String categoryName, Pageable pageable);


    List<Category> findAllByName(String childName);

    List<Category> findByParentIsNull();

    Long countByParentIsNull();

    Optional<Category> findByIdAndParentIsNull(Long id);

}
