package com.badran.store.repository;

import com.badran.store.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository for cart item lines.
 */
@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    /**
     * Finds a product line inside a specific cart.
     */
    Optional<CartItem> findByCartCartIdAndProductId(Long cartId, Long productId);
}
