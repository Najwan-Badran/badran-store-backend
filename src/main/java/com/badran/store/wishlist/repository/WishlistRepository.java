package com.badran.store.wishlist.repository;

import com.badran.store.wishlist.entity.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WishlistRepository extends JpaRepository<Wishlist, Long> {
    List<Wishlist> findByUserId(Long userId);
    boolean existsByUserIdAndProductProductId(Long userId, Long productId);
    void deleteByUserIdAndProductProductId(Long userId, Long productId);
}
