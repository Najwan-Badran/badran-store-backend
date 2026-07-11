package com.badran.store.repository;

import com.badran.store.entity.Product;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository for product catalog lookup, searching, and stock locking.
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    /**
     * Finds a product detail record with associations needed by API mapping.
     */
    @Override
    @EntityGraph(attributePaths = {"category", "brand", "images"})
    Optional<Product> findById(Long productId);

    /**
     * Finds a product by id using a pessimistic write lock for stock deduction.
     */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT p FROM Product p WHERE p.productId = :productId")
    Optional<Product> findByIdForUpdate(@Param("productId") Long productId);

    /**
     * Searches active catalog products by optional category, brand, and localized text query.
     */
    @Query("SELECT p FROM Product p WHERE " +
           "(:categoryId IS NULL OR p.category.categoryId = :categoryId) AND " +
           "(:brandId IS NULL OR p.brand.brandId = :brandId) AND " +
           "(COALESCE(CAST(:searchQuery AS string), '') = '' OR " +
           " LOWER(p.nameEn) LIKE LOWER(CONCAT('%', CAST(:searchQuery AS string), '%')) OR " +
           " LOWER(p.nameAr) LIKE LOWER(CONCAT('%', CAST(:searchQuery AS string), '%')) OR " +
           " LOWER(p.descriptionEn) LIKE LOWER(CONCAT('%', CAST(:searchQuery AS string), '%')) OR " +
           " LOWER(p.descriptionAr) LIKE LOWER(CONCAT('%', CAST(:searchQuery AS string), '%')))")
    @EntityGraph(attributePaths = {"category", "brand"})
    Page<Product> searchProducts(
            @Param("categoryId") Long categoryId,
            @Param("brandId") Long brandId,
            @Param("searchQuery") String searchQuery,
            Pageable pageable
    );
}
