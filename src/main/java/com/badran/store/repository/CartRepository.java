package com.badran.store.repository;

import com.badran.store.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository for carts owned by authenticated users or anonymous sessions.
 */
@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    /**
     * Finds the active cart for an authenticated user.
     */
    Optional<Cart> findByUserId(Long userId);

    /**
     * Finds the active cart for an anonymous session.
     */
    Optional<Cart> findBySessionId(String sessionId);
}
