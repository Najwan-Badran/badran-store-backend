package com.badran.store.repository;

import com.badran.store.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for product category hierarchy records.
 */
@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
}
