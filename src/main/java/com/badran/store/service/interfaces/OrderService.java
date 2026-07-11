package com.badran.store.service.interfaces;

import com.badran.store.dto.request.AddToCartRequest;
import com.badran.store.dto.model.CartDto;
import com.badran.store.dto.request.CreateOrderRequest;
import com.badran.store.dto.model.OrderDto;
import com.badran.store.dto.model.PaymentDto;

import java.util.List;

/**
 * Application service contract for cart, checkout, order lookup, and payment workflows.
 */
public interface OrderService {
    /**
     * Retrieves the current cart for an authenticated user or anonymous session.
     */
    CartDto getCart(Long userId, String sessionId);

    /**
     * Adds a product quantity to the current cart.
     */
    CartDto addItemToCart(Long userId, String sessionId, AddToCartRequest request);

    /**
     * Removes one product from the current cart.
     */
    CartDto removeItemFromCart(Long userId, String sessionId, Long productId);

    /**
     * Removes all items from the current cart.
     */
    void clearCart(Long userId, String sessionId);

    /**
     * Converts the current cart into an order and creates the initial payment record.
     */
    OrderDto createOrder(Long userId, String sessionId, CreateOrderRequest request);

    /**
     * Retrieves an order by its internal identifier.
     */
    OrderDto getOrderById(Long orderId);

    /**
     * Lists orders owned by a user.
     */
    List<OrderDto> getOrdersByUser(Long userId);

    /**
     * Completes a pending order payment with a supported payment method.
     */
    PaymentDto payForOrder(Long orderId, String paymentMethod);
}
