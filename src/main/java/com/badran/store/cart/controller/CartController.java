package com.badran.store.cart.controller;

import com.badran.store.cart.dto.AddToCartRequest;
import com.badran.store.cart.dto.CartDto;
import com.badran.store.common.dto.ApiResponse;
import com.badran.store.order.service.OrderService;
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

@RestController
@RequestMapping("/api/v1/cart")
@RequiredArgsConstructor
public class CartController {

    private final OrderService orderService;

    @GetMapping
    public ResponseEntity<ApiResponse<CartDto>> getCart(
            @RequestHeader(value = "X-User-Id", required = false) Long userId,
            @RequestHeader(value = "X-Session-Id", required = false) String sessionId) {
        CartDto cart = orderService.getCart(userId, sessionId);
        return ResponseEntity.ok(ApiResponse.success(cart, "Cart retrieved successfully"));
    }

    @PostMapping("/items")
    public ResponseEntity<ApiResponse<CartDto>> addItemToCart(
            @RequestHeader(value = "X-User-Id", required = false) Long userId,
            @RequestHeader(value = "X-Session-Id", required = false) String sessionId,
            @Valid @RequestBody AddToCartRequest request) {
        CartDto cart = orderService.addItemToCart(userId, sessionId, request);
        return ResponseEntity.ok(ApiResponse.success(cart, "Product added to cart"));
    }

    @DeleteMapping("/items/{productId}")
    public ResponseEntity<ApiResponse<CartDto>> removeItemFromCart(
            @RequestHeader(value = "X-User-Id", required = false) Long userId,
            @RequestHeader(value = "X-Session-Id", required = false) String sessionId,
            @PathVariable Long productId) {
        CartDto cart = orderService.removeItemFromCart(userId, sessionId, productId);
        return ResponseEntity.ok(ApiResponse.success(cart, "Product removed from cart"));
    }

    @DeleteMapping
    public ResponseEntity<ApiResponse<Void>> clearCart(
            @RequestHeader(value = "X-User-Id", required = false) Long userId,
            @RequestHeader(value = "X-Session-Id", required = false) String sessionId) {
        orderService.clearCart(userId, sessionId);
        return ResponseEntity.ok(ApiResponse.success(null, "Cart cleared successfully"));
    }
}
