package com.badran.store.category.repository;

import com.badran.store.category.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for product category hierarchy records.
 */
@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    /**
     * Finds top-level catalog categories.
     */
    List<Category> findByParentCategoryIsNull();
}
