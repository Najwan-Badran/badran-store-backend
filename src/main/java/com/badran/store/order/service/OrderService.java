package com.badran.store.order.service;

import com.badran.store.cart.dto.AddToCartRequest;
import com.badran.store.cart.dto.CartDto;
import com.badran.store.order.dto.*;

import java.util.List;

public interface OrderService {
    // Cart
    CartDto getCart(Long userId, String sessionId);
    CartDto addItemToCart(Long userId, String sessionId, AddToCartRequest request);
    CartDto removeItemFromCart(Long userId, String sessionId, Long productId);
    void clearCart(Long userId, String sessionId);

    // Orders
    OrderDto createOrder(Long userId, String sessionId, CreateOrderRequest request);
    OrderDto getOrderById(Long orderId);
    List<OrderDto> getOrdersByUser(Long userId);
    PaymentDto payForOrder(Long orderId, String paymentMethod);
}
