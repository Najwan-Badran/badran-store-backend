package com.badran.store.order.controller;

import com.badran.store.common.dto.ApiResponse;
import com.badran.store.order.dto.CreateOrderRequest;
import com.badran.store.order.dto.OrderDto;
import com.badran.store.order.dto.PaymentDto;
import com.badran.store.order.service.OrderService;
import com.badran.store.security.SecurityContextService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST controller for checkout, order lookup, and payment operations.
 */
@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
@Tag(name = "Orders", description = "Checkout, order history, order lookup, and payment endpoints.")
public class OrderController {

    private final OrderService orderService;
    private final SecurityContextService securityContextService;

    /**
     * Creates an order from the current cart.
     */
    @Operation(
            summary = "Create order",
            description = "Converts the current cart into an order, applies coupons and delivery fee rules, deducts stock, and creates the initial payment record.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Order created"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Cart is empty, payment method is invalid, or stock is insufficient")
    })
    @PostMapping
    public ResponseEntity<ApiResponse<OrderDto>> createOrder(
            @RequestHeader(value = "X-User-Id", required = false) Long userId,
            @RequestHeader(value = "X-Session-Id", required = false) String sessionId,
            @Valid @RequestBody CreateOrderRequest request) {
        OrderDto order = orderService.createOrder(userId, sessionId, request);
        return ResponseEntity.ok(ApiResponse.success(order, "Order created successfully"));
    }

    /**
     * Retrieves an order after ownership or admin authorization.
     */
    @Operation(
            summary = "Get order",
            description = "Returns a single order after validating that the caller owns the order or has admin privileges.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Order retrieved"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Order does not belong to the authenticated user"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Order not found")
    })
    @GetMapping("/{orderId}")
    public ResponseEntity<ApiResponse<OrderDto>> getOrderById(
            @RequestHeader("X-User-Id") Long userId,
            @PathVariable Long orderId) {
        OrderDto order = orderService.getOrderById(orderId);
        securityContextService.assertAdminOrOwner(userId, order.getUserId(), "Order does not belong to authenticated user");
        return ResponseEntity.ok(ApiResponse.success(order, "Order retrieved successfully"));
    }

    /**
     * Lists orders for the authenticated user id header.
     */
    @Operation(
            summary = "List user orders",
            description = "Returns all orders associated with the authenticated user id header.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Orders retrieved"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "JWT is missing or invalid")
    })
    @GetMapping
    public ResponseEntity<ApiResponse<List<OrderDto>>> getOrdersByUser(@RequestHeader("X-User-Id") Long userId) {
        List<OrderDto> orders = orderService.getOrdersByUser(userId);
        return ResponseEntity.ok(ApiResponse.success(orders, "Orders retrieved successfully"));
    }

    /**
     * Completes payment for an order after ownership or admin authorization.
     */
    @Operation(
            summary = "Pay for order",
            description = "Marks a pending order payment as paid using a supported payment method.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Payment completed"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid payment method or no pending payment exists"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Order does not belong to the authenticated user")
    })
    @PostMapping("/{orderId}/payment")
    public ResponseEntity<ApiResponse<PaymentDto>> payForOrder(
            @RequestHeader("X-User-Id") Long userId,
            @PathVariable Long orderId,
            @RequestParam String paymentMethod) {
        OrderDto order = orderService.getOrderById(orderId);
        securityContextService.assertAdminOrOwner(userId, order.getUserId(), "Order does not belong to authenticated user");
        PaymentDto payment = orderService.payForOrder(orderId, paymentMethod);
        return ResponseEntity.ok(ApiResponse.success(payment, "Payment completed successfully"));
    }
}
