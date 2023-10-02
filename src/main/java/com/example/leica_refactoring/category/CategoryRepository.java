package com.example.leica_refactoring.category;

import com.example.leica_refactoring.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

//    Optional<Category> findById(Long categoryId);

    Category findByName(String categoryName);

    List<Category> findAllByName(String childName);

    List<Category> findByParentIsNull();
}
