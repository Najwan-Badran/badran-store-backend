package com.badran.store.controller;

import com.badran.store.dto.response.ApiResponse;
import com.badran.store.dto.model.OrderDto;
import com.badran.store.dto.model.ProductDto;
import com.badran.store.dto.model.ReviewDto;
import com.badran.store.dto.model.WishlistDto;
import com.badran.store.security.SecurityContextService;
import com.badran.store.service.interfaces.OrderService;
import com.badran.store.service.interfaces.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST controller for product catalog, inventory, wishlist, and review operations.
 */
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Validated
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
            @RequestParam(required = false) @Positive Long categoryId,
            @RequestParam(required = false) @Positive Long brandId,
            @RequestParam(required = false) @Size(max = 255) String search,
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "10") @Min(1) @Max(100) int size,
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
    public ResponseEntity<ApiResponse<ProductDto>> getProductById(@PathVariable @Positive Long id) {
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
            @PathVariable @Positive Long id,
            @RequestParam @Positive Integer quantity) {
        ProductDto product = productService.verifyAndDeductStock(id, quantity);
        return ResponseEntity.ok(product);
    }

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
            @RequestHeader("X-User-Id") @Positive Long userId) {
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
            @RequestHeader("X-User-Id") @Positive Long userId,
            @PathVariable @Positive Long productId) {
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
            @RequestHeader("X-User-Id") @Positive Long userId,
            @PathVariable @Positive Long productId) {
        productService.removeFromWishlist(userId, productId);
        return ResponseEntity.ok(ApiResponse.success(null, "Product removed from wishlist"));
    }

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
    public ResponseEntity<ApiResponse<List<ReviewDto>>> getProductReviews(@PathVariable @Positive Long productId) {
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
            @RequestHeader("X-User-Id") @Positive Long userId,
            @PathVariable @Positive Long productId,
            @RequestParam @Positive Long orderId,
            @RequestParam @Min(1) @Max(5) Integer rating,
            @RequestParam(required = false) @Size(max = 5000) String comment) {
        OrderDto order = orderService.getOrderById(orderId);
        securityContextService.assertAdminOrOwner(userId, order.getUserId(), "Order does not belong to authenticated user");
        ReviewDto review = productService.addReview(userId, productId, orderId, rating, comment);
        return ResponseEntity.ok(ApiResponse.success(review, "Review added successfully"));
    }
}
