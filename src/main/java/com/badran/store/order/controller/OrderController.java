package com.badran.store.order.controller;

import com.badran.store.common.dto.ApiResponse;
import com.badran.store.order.dto.CreateOrderRequest;
import com.badran.store.order.dto.OrderDto;
import com.badran.store.order.dto.PaymentDto;
import com.badran.store.order.service.OrderService;
import com.badran.store.security.UserPrincipal;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
import java.util.Objects;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<ApiResponse<OrderDto>> createOrder(
            @RequestHeader(value = "X-User-Id", required = false) Long userId,
            @RequestHeader(value = "X-Session-Id", required = false) String sessionId,
            @Valid @RequestBody CreateOrderRequest request) {
        OrderDto order = orderService.createOrder(userId, sessionId, request);
        return ResponseEntity.ok(ApiResponse.success(order, "Order created successfully"));
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<ApiResponse<OrderDto>> getOrderById(
            @RequestHeader("X-User-Id") Long userId,
            @PathVariable Long orderId) {
        OrderDto order = orderService.getOrderById(orderId);
        assertCanAccessOrder(userId, order);
        return ResponseEntity.ok(ApiResponse.success(order, "Order retrieved successfully"));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<OrderDto>>> getOrdersByUser(@RequestHeader("X-User-Id") Long userId) {
        List<OrderDto> orders = orderService.getOrdersByUser(userId);
        return ResponseEntity.ok(ApiResponse.success(orders, "Orders retrieved successfully"));
    }

    @PostMapping("/{orderId}/payment")
    public ResponseEntity<ApiResponse<PaymentDto>> payForOrder(
            @RequestHeader("X-User-Id") Long userId,
            @PathVariable Long orderId,
            @RequestParam String paymentMethod) {
        assertCanAccessOrder(userId, orderService.getOrderById(orderId));
        PaymentDto payment = orderService.payForOrder(orderId, paymentMethod);
        return ResponseEntity.ok(ApiResponse.success(payment, "Payment completed successfully"));
    }

    private void assertCanAccessOrder(Long userId, OrderDto order) {
        if (isAdmin()) {
            return;
        }
        if (!Objects.equals(userId, order.getUserId())) {
            throw new AccessDeniedException("Order does not belong to authenticated user");
        }
    }

    private boolean isAdmin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null
                && authentication.getPrincipal() instanceof UserPrincipal principal
                && "admin".equalsIgnoreCase(principal.role());
    }
}
