package com.badran.store.cart.controller;

import com.badran.store.cart.dto.AddToCartRequest;
import com.badran.store.cart.dto.CartDto;
import com.badran.store.common.dto.ApiResponse;
import com.badran.store.order.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for cart retrieval and cart item mutations.
 */
@RestController
@RequestMapping("/api/v1/cart")
@RequiredArgsConstructor
@Tag(name = "Cart", description = "Shopping cart endpoints for authenticated users and anonymous sessions.")
public class CartController {

    private final OrderService orderService;

    /**
     * Retrieves the current cart for a user or anonymous session.
     */
    @Operation(
            summary = "Get cart",
            description = "Returns the active cart for the X-User-Id header or the anonymous X-Session-Id header.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Cart retrieved"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Neither user id nor session id was supplied")
    })
    @GetMapping
    public ResponseEntity<ApiResponse<CartDto>> getCart(
            @RequestHeader(value = "X-User-Id", required = false) Long userId,
            @RequestHeader(value = "X-Session-Id", required = false) String sessionId) {
        CartDto cart = orderService.getCart(userId, sessionId);
        return ResponseEntity.ok(ApiResponse.success(cart, "Cart retrieved successfully"));
    }

    /**
     * Adds a product quantity to the current cart.
     */
    @Operation(
            summary = "Add cart item",
            description = "Adds a product quantity to the current cart or increases the existing cart line quantity.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Product added to cart"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Validation failed"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Product not found")
    })
    @PostMapping("/items")
    public ResponseEntity<ApiResponse<CartDto>> addItemToCart(
            @RequestHeader(value = "X-User-Id", required = false) Long userId,
            @RequestHeader(value = "X-Session-Id", required = false) String sessionId,
            @Valid @RequestBody AddToCartRequest request) {
        CartDto cart = orderService.addItemToCart(userId, sessionId, request);
        return ResponseEntity.ok(ApiResponse.success(cart, "Product added to cart"));
    }

    /**
     * Removes a product from the current cart.
     */
    @Operation(
            summary = "Remove cart item",
            description = "Removes a product line from the current cart.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Product removed from cart"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Product was not found in cart")
    })
    @DeleteMapping("/items/{productId}")
    public ResponseEntity<ApiResponse<CartDto>> removeItemFromCart(
            @RequestHeader(value = "X-User-Id", required = false) Long userId,
            @RequestHeader(value = "X-Session-Id", required = false) String sessionId,
            @PathVariable Long productId) {
        CartDto cart = orderService.removeItemFromCart(userId, sessionId, productId);
        return ResponseEntity.ok(ApiResponse.success(cart, "Product removed from cart"));
    }

    /**
     * Clears all items from the current cart.
     */
    @Operation(
            summary = "Clear cart",
            description = "Deletes all item lines from the current cart.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Cart cleared")
    })
    @DeleteMapping
    public ResponseEntity<ApiResponse<Void>> clearCart(
            @RequestHeader(value = "X-User-Id", required = false) Long userId,
            @RequestHeader(value = "X-Session-Id", required = false) String sessionId) {
        orderService.clearCart(userId, sessionId);
        return ResponseEntity.ok(ApiResponse.success(null, "Cart cleared successfully"));
    }
}
