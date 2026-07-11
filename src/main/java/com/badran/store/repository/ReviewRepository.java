package com.badran.store.repository;

import com.badran.store.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for product review lookup.
 */
@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    /**
     * Finds reviews for a product filtered by moderation status.
     */
    List<Review> findByProductProductIdAndStatus(Long productId, String status);
}
