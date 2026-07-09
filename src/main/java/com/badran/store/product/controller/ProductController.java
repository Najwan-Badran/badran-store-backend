package com.badran.store.product.controller;

import com.badran.store.common.dto.ApiResponse;
import com.badran.store.product.dto.ProductDto;
import com.badran.store.review.dto.ReviewDto;
import com.badran.store.wishlist.dto.WishlistDto;
import com.badran.store.order.dto.OrderDto;
import com.badran.store.order.service.OrderService;
import com.badran.store.product.service.ProductService;
import com.badran.store.security.SecurityContextService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for product catalog, inventory, wishlist, and review operations.
 */
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Tag(name = "Catalog", description = "Product catalog, stock, wishlist, and review endpoints.")
public class ProductController {

    private final ProductService productService;
    private final OrderService orderService;
    private final SecurityContextService securityContextService;

    /**
     * Searches products using optional filters, pagination, and sorting.
     */
    @Operation(
            summary = "Search products",
            description = "Returns a paginated product catalog filtered by category, brand, or full-text style search query.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Product page retrieved")
    })
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

    /**
     * Retrieves a product by id.
     */
    @Operation(
            summary = "Get product",
            description = "Returns one catalog product, including category, brand, images, stock, and review summary fields.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Product retrieved"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Product not found")
    })
    @GetMapping("/products/{id}")
    public ResponseEntity<ApiResponse<ProductDto>> getProductById(@PathVariable Long id) {
        ProductDto product = productService.getProductById(id);
        return ResponseEntity.ok(ApiResponse.success(product));
    }

    /**
     * Deducts product stock for internal/admin workflows.
     */
    @Operation(
            summary = "Deduct stock",
            description = "Deducts inventory for a product. This endpoint is intended for protected internal or admin stock workflows.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Stock deducted"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Quantity is non-positive or stock is insufficient"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Product not found")
    })
    @PutMapping("/products/internal/{id}/deduct-stock")
    public ResponseEntity<ProductDto> deductStock(
            @PathVariable Long id, 
            @RequestParam Integer quantity) {
        ProductDto product = productService.verifyAndDeductStock(id, quantity);
        return ResponseEntity.ok(product);
    }

    // Wishlist endpoints
    /**
     * Lists wishlist entries for the authenticated user.
     */
    @Operation(
            summary = "Get wishlist",
            description = "Returns all wishlist entries for the authenticated user.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Wishlist retrieved"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "JWT is missing or invalid")
    })
    @GetMapping("/wishlist")
    public ResponseEntity<ApiResponse<List<WishlistDto>>> getWishlist(
            @RequestHeader("X-User-Id") Long userId) {
        List<WishlistDto> wishlist = productService.getWishlist(userId);
        return ResponseEntity.ok(ApiResponse.success(wishlist));
    }

    /**
     * Adds a product to the authenticated user's wishlist.
     */
    @Operation(
            summary = "Add wishlist item",
            description = "Adds a product to the authenticated user's wishlist.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Product added to wishlist"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Product is already in wishlist"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Product not found")
    })
    @PostMapping("/wishlist/{productId}")
    public ResponseEntity<ApiResponse<Void>> addToWishlist(
            @RequestHeader("X-User-Id") Long userId,
            @PathVariable Long productId) {
        productService.addToWishlist(userId, productId);
        return ResponseEntity.ok(ApiResponse.success(null, "Product added to wishlist"));
    }

    /**
     * Removes a product from the authenticated user's wishlist.
     */
    @Operation(
            summary = "Remove wishlist item",
            description = "Removes a product from the authenticated user's wishlist.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Product removed from wishlist")
    })
    @DeleteMapping("/wishlist/{productId}")
    public ResponseEntity<ApiResponse<Void>> removeFromWishlist(
            @RequestHeader("X-User-Id") Long userId,
            @PathVariable Long productId) {
        productService.removeFromWishlist(userId, productId);
        return ResponseEntity.ok(ApiResponse.success(null, "Product removed from wishlist"));
    }

    // Review endpoints
    /**
     * Lists published reviews for a product.
     */
    @Operation(
            summary = "Get product reviews",
            description = "Returns published reviews for a product.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Reviews retrieved")
    })
    @GetMapping("/reviews/product/{productId}")
    public ResponseEntity<ApiResponse<List<ReviewDto>>> getProductReviews(@PathVariable Long productId) {
        List<ReviewDto> reviews = productService.getProductReviews(productId);
        return ResponseEntity.ok(ApiResponse.success(reviews));
    }

    /**
     * Adds a review for a product after verifying order ownership.
     */
    @Operation(
            summary = "Add product review",
            description = "Adds a published product review after verifying the referenced order belongs to the caller or caller is admin.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Review added"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Rating or review request is invalid"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Referenced order does not belong to the authenticated user")
    })
    @PostMapping("/reviews/product/{productId}")
    public ResponseEntity<ApiResponse<ReviewDto>> addReview(
            @RequestHeader("X-User-Id") Long userId,
            @PathVariable Long productId,
            @RequestParam Long orderId,
            @RequestParam Integer rating,
            @RequestParam(required = false) String comment) {
        OrderDto order = orderService.getOrderById(orderId);
        securityContextService.assertAdminOrOwner(userId, order.getUserId(), "Order does not belong to authenticated user");
        ReviewDto review = productService.addReview(userId, productId, orderId, rating, comment);
        return ResponseEntity.ok(ApiResponse.success(review, "Review added successfully"));
    }
}
