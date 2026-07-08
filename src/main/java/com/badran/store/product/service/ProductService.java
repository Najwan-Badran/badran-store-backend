package com.badran.store.product.service;

import com.badran.store.product.dto.ProductDto;
import com.badran.store.review.dto.ReviewDto;
import com.badran.store.wishlist.dto.WishlistDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductService {
    Page<ProductDto> getProducts(Long categoryId, Long brandId, String query, Pageable pageable);
    ProductDto getProductById(Long productId);
    ProductDto verifyAndDeductStock(Long productId, Integer quantity);
    
    // Wishlist
    List<WishlistDto> getWishlist(Long userId);
    void addToWishlist(Long userId, Long productId);
    void removeFromWishlist(Long userId, Long productId);
    
    // Reviews
    List<ReviewDto> getProductReviews(Long productId);
    ReviewDto addReview(Long userId, Long productId, Long orderId, Integer rating, String comment);
}
