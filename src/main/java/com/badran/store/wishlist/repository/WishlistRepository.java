package com.badran.store.wishlist.repository;

import com.badran.store.wishlist.entity.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for wishlist entries owned by users.
 */
@Repository
public interface WishlistRepository extends JpaRepository<Wishlist, Long> {
    /**
     * Finds all wishlist entries for a user.
     */
    List<Wishlist> findByUserId(Long userId);

    /**
     * Checks whether a user already saved a product.
     */
    boolean existsByUserIdAndProductProductId(Long userId, Long productId);

    /**
     * Removes a saved product from a user's wishlist.
     */
    void deleteByUserIdAndProductProductId(Long userId, Long productId);
}
