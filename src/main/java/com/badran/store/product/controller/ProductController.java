package com.badran.store.product.controller;

import com.badran.store.common.dto.ApiResponse;
import com.badran.store.product.dto.ProductDto;
import com.badran.store.review.dto.ReviewDto;
import com.badran.store.wishlist.dto.WishlistDto;
import com.badran.store.order.dto.OrderDto;
import com.badran.store.order.service.OrderService;
import com.badran.store.product.service.ProductService;
import com.badran.store.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final OrderService orderService;

    @GetMapping("/products")
    public ResponseEntity<ApiResponse<Page<ProductDto>>> getProducts(
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Long brandId,
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "productId") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.DESC.name()) 
                ? Sort.by(sortBy).descending() 
                : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        
        Page<ProductDto> products = productService.getProducts(categoryId, brandId, search, pageable);
        return ResponseEntity.ok(ApiResponse.success(products));
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<ApiResponse<ProductDto>> getProductById(@PathVariable Long id) {
        ProductDto product = productService.getProductById(id);
        return ResponseEntity.ok(ApiResponse.success(product));
    }

    @PutMapping("/products/internal/{id}/deduct-stock")
    public ResponseEntity<ProductDto> deductStock(
            @PathVariable Long id, 
            @RequestParam Integer quantity) {
        ProductDto product = productService.verifyAndDeductStock(id, quantity);
        return ResponseEntity.ok(product);
    }

    // Wishlist endpoints
    @GetMapping("/wishlist")
    public ResponseEntity<ApiResponse<List<WishlistDto>>> getWishlist(
            @RequestHeader("X-User-Id") Long userId) {
        List<WishlistDto> wishlist = productService.getWishlist(userId);
        return ResponseEntity.ok(ApiResponse.success(wishlist));
    }

    @PostMapping("/wishlist/{productId}")
    public ResponseEntity<ApiResponse<Void>> addToWishlist(
            @RequestHeader("X-User-Id") Long userId,
            @PathVariable Long productId) {
        productService.addToWishlist(userId, productId);
        return ResponseEntity.ok(ApiResponse.success(null, "Product added to wishlist"));
    }

    @DeleteMapping("/wishlist/{productId}")
    public ResponseEntity<ApiResponse<Void>> removeFromWishlist(
            @RequestHeader("X-User-Id") Long userId,
            @PathVariable Long productId) {
        productService.removeFromWishlist(userId, productId);
        return ResponseEntity.ok(ApiResponse.success(null, "Product removed from wishlist"));
    }

    // Review endpoints
    @GetMapping("/reviews/product/{productId}")
    public ResponseEntity<ApiResponse<List<ReviewDto>>> getProductReviews(@PathVariable Long productId) {
        List<ReviewDto> reviews = productService.getProductReviews(productId);
        return ResponseEntity.ok(ApiResponse.success(reviews));
    }

    @PostMapping("/reviews/product/{productId}")
    public ResponseEntity<ApiResponse<ReviewDto>> addReview(
            @RequestHeader("X-User-Id") Long userId,
            @PathVariable Long productId,
            @RequestParam Long orderId,
            @RequestParam Integer rating,
            @RequestParam(required = false) String comment) {
        OrderDto order = orderService.getOrderById(orderId);
        if (!isAdmin() && !Objects.equals(userId, order.getUserId())) {
            throw new AccessDeniedException("Order does not belong to authenticated user");
        }
        ReviewDto review = productService.addReview(userId, productId, orderId, rating, comment);
        return ResponseEntity.ok(ApiResponse.success(review, "Review added successfully"));
    }

    private boolean isAdmin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null
                && authentication.getPrincipal() instanceof UserPrincipal principal
                && "admin".equalsIgnoreCase(principal.role());
    }
}
