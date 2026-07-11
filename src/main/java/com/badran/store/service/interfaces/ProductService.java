package com.badran.store.service.interfaces;

import com.badran.store.dto.model.ProductDto;
import com.badran.store.dto.model.ReviewDto;
import com.badran.store.dto.model.WishlistDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Application service contract for product catalog, inventory, wishlist, and review workflows.
 */
public interface ProductService {
    /**
     * Searches active product catalog data with optional filters and pagination.
     */
    Page<ProductDto> getProducts(Long categoryId, Long brandId, String query, Pageable pageable);

    /**
     * Retrieves a single product by identifier.
     */
    ProductDto getProductById(Long productId);

    /**
     * Verifies available stock and atomically deducts the requested quantity.
     */
    ProductDto verifyAndDeductStock(Long productId, Integer quantity);
    
    /**
     * Lists wishlist items for a user.
     */
    List<WishlistDto> getWishlist(Long userId);

    /**
     * Adds a product to a user's wishlist.
     */
    void addToWishlist(Long userId, Long productId);

    /**
     * Removes a product from a user's wishlist.
     */
    void removeFromWishlist(Long userId, Long productId);
    
    /**
     * Lists published product reviews.
     */
    List<ReviewDto> getProductReviews(Long productId);

    /**
     * Adds a product review tied to an order.
     */
    ReviewDto addReview(Long userId, Long productId, Long orderId, Integer rating, String comment);
}
