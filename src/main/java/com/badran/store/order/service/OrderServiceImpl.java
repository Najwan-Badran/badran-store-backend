package com.badran.store.order.service;

import com.badran.store.cart.dto.AddToCartRequest;
import com.badran.store.cart.dto.CartDto;
import com.badran.store.cart.entity.Cart;
import com.badran.store.cart.entity.CartItem;
import com.badran.store.cart.mapper.CartMapper;
import com.badran.store.cart.repository.CartItemRepository;
import com.badran.store.cart.repository.CartRepository;
import com.badran.store.coupon.entity.Coupon;
import com.badran.store.coupon.repository.CouponRepository;
import com.badran.store.exception.BadRequestException;
import com.badran.store.exception.ResourceNotFoundException;
import com.badran.store.order.dto.*;
import com.badran.store.order.entity.*;
import com.badran.store.order.mapper.*;
import com.badran.store.order.repository.*;
import com.badran.store.product.dto.ProductDto;
import com.badran.store.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final PaymentRepository paymentRepository;
    private final CouponRepository couponRepository;

    private final ProductService productService;

    private final CartMapper cartMapper;
    private final OrderMapper orderMapper;
    private final PaymentMapper paymentMapper;

    @Override
    @Transactional
    public CartDto getCart(Long userId, String sessionId) {
        Cart cart = getOrCreateCart(userId, sessionId);
        return cartMapper.toDto(cart);
    }

    private Cart getOrCreateCart(Long userId, String sessionId) {
        if (userId != null) {
            return cartRepository.findByUserId(userId)
                    .orElseGet(() -> cartRepository.save(Cart.builder().userId(userId).build()));
        } else if (sessionId != null) {
            return cartRepository.findBySessionId(sessionId)
                    .orElseGet(() -> cartRepository.save(Cart.builder().sessionId(sessionId).build()));
        } else {
            throw new BadRequestException("Either User ID or Session ID must be provided to retrieve cart");
        }
    }

    @Override
    @Transactional
    public CartDto addItemToCart(Long userId, String sessionId, AddToCartRequest request) {
        if (request.getQuantity() == null || request.getQuantity() <= 0) {
            throw new BadRequestException("Quantity must be greater than zero");
        }

        Cart cart = getOrCreateCart(userId, sessionId);
        
        productService.getProductById(request.getProductId());

        CartItem item = cartItemRepository.findByCartCartIdAndProductId(cart.getCartId(), request.getProductId())
                .orElseGet(() -> CartItem.builder().cart(cart).productId(request.getProductId()).quantity(0).build());

        item.setQuantity(item.getQuantity() + request.getQuantity());
        cartItemRepository.save(item);
        
        return cartMapper.toDto(getOrCreateCart(userId, sessionId));
    }

    @Override
    @Transactional
    public CartDto removeItemFromCart(Long userId, String sessionId, Long productId) {
        Cart cart = getOrCreateCart(userId, sessionId);
        CartItem item = cartItemRepository.findByCartCartIdAndProductId(cart.getCartId(), productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found in cart"));

        cart.getItems().removeIf(cartItem -> cartItem.getCartItemId().equals(item.getCartItemId()));
        cartItemRepository.delete(item);
        cartItemRepository.flush();
        return cartMapper.toDto(getOrCreateCart(userId, sessionId));
    }

    @Override
    @Transactional
    public void clearCart(Long userId, String sessionId) {
        Cart cart = getOrCreateCart(userId, sessionId);
        cart.getItems().clear();
        cartRepository.save(cart);
    }

    @Override
    @Transactional
    public OrderDto createOrder(Long userId, String sessionId, CreateOrderRequest request) {
        String fulfillmentMethod = normalizeFulfillmentMethod(request.getFulfillmentMethod());
        String paymentMethod = normalizePaymentMethod(request.getPaymentMethod());
        Cart cart = getOrCreateCart(userId, sessionId);
        if (cart.getItems().isEmpty()) {
            throw new BadRequestException("Cannot checkout an empty cart");
        }

        BigDecimal subtotal = BigDecimal.ZERO;
        
        // 1. Fetch prices, validate stock, and calculate subtotal
        for (CartItem item : cart.getItems()) {
            ProductDto product = productService.getProductById(item.getProductId());
            if (product.getStockQuantity() < item.getQuantity()) {
                throw new BadRequestException("Product " + product.getNameEn() + " has insufficient stock");
            }
            BigDecimal lineTotal = product.getBasePrice().multiply(BigDecimal.valueOf(item.getQuantity()));
            subtotal = subtotal.add(lineTotal);
        }

        // 2. Validate and apply coupon if provided
        BigDecimal discountAmount = BigDecimal.ZERO;
        Coupon coupon = null;
        if (request.getCouponCode() != null && !request.getCouponCode().trim().isEmpty()) {
            coupon = couponRepository.findByCodeIgnoreCaseForUpdate(request.getCouponCode().trim())
                    .orElseThrow(() -> new BadRequestException("Invalid coupon code"));

            if (!coupon.getIsActive() || coupon.getValidFrom().isAfter(LocalDate.now()) || coupon.getValidTo().isBefore(LocalDate.now())) {
                throw new BadRequestException("Coupon is expired or inactive");
            }

            if (coupon.getUsageLimit() != null && coupon.getUsageCount() >= coupon.getUsageLimit()) {
                throw new BadRequestException("Coupon usage limit exceeded");
            }

            if ("percentage".equalsIgnoreCase(coupon.getType())) {
                discountAmount = subtotal.multiply(coupon.getValue().divide(BigDecimal.valueOf(100)));
            } else if ("fixed_amount".equalsIgnoreCase(coupon.getType())) {
                discountAmount = coupon.getValue();
            }
            coupon.setUsageCount(coupon.getUsageCount() + 1);
            couponRepository.save(coupon);
        }

        // 3. Delivery fee
        BigDecimal deliveryFee = BigDecimal.ZERO;
        if ("home_delivery".equalsIgnoreCase(fulfillmentMethod)) {
            deliveryFee = BigDecimal.valueOf(10.0); // Flat fee
        }

        BigDecimal total = subtotal.subtract(discountAmount).add(deliveryFee);
        if (total.compareTo(BigDecimal.ZERO) < 0) {
            total = BigDecimal.ZERO;
        }

        // 4. Create Order
        Order order = Order.builder()
                .publicId(UUID.randomUUID())
                .orderNumber("ORD-" + System.currentTimeMillis())
                .userId(userId)
                .guestName(request.getGuestName())
                .guestPhone(request.getGuestPhone())
                .guestEmail(request.getGuestEmail())
                .fulfillmentMethod(fulfillmentMethod)
                .deliveryAddressId(request.getDeliveryAddressId())
                .deliveryCity(request.getDeliveryCity())
                .deliveryZone(request.getDeliveryZone())
                .deliveryAddressLine(request.getDeliveryAddressLine())
                .deliveryFee(deliveryFee)
                .status("pending")
                .subtotal(subtotal)
                .coupon(coupon)
                .discountAmount(discountAmount)
                .total(total)
                .build();

        Order savedOrder = orderRepository.save(order);

        // 5. Create Order Items & Deduct Stock
        for (CartItem item : cart.getItems()) {
            ProductDto product = productService.getProductById(item.getProductId());
            
            productService.verifyAndDeductStock(item.getProductId(), item.getQuantity());

            OrderItem orderItem = OrderItem.builder()
                    .order(savedOrder)
                    .productId(item.getProductId())
                    .quantity(item.getQuantity())
                    .unitPrice(product.getBasePrice())
                    .lineTotal(product.getBasePrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                    .build();
            orderItemRepository.save(orderItem);
            savedOrder.getItems().add(orderItem);
        }

        // 6. Create Payment Record
        Payment payment = Payment.builder()
                .order(savedOrder)
                .method(paymentMethod)
                .status("unpaid")
                .build();
        paymentRepository.save(payment);

        // 7. Clear Cart
        cartItemRepository.deleteAll(cart.getItems());
        cart.getItems().clear();
        cartRepository.save(cart);

        return orderMapper.toDto(savedOrder);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderDto getOrderById(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with ID: " + orderId));
        return orderMapper.toDto(order);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderDto> getOrdersByUser(Long userId) {
        return orderRepository.findByUserId(userId).stream()
                .map(orderMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public PaymentDto payForOrder(Long orderId, String paymentMethod) {
        String normalizedPaymentMethod = normalizePaymentMethod(paymentMethod);
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        List<Payment> payments = paymentRepository.findByOrderOrderId(orderId);
        Payment payment = payments.stream()
                .filter(p -> "unpaid".equalsIgnoreCase(p.getStatus()) || "pending_verification".equalsIgnoreCase(p.getStatus()))
                .findFirst()
                .orElseThrow(() -> new BadRequestException("No pending payment found for this order"));

        payment.setMethod(normalizedPaymentMethod);
        payment.setStatus("paid");
        payment.setTransactionRef("TXN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        payment.setVerifiedAt(OffsetDateTime.now());
        paymentRepository.save(payment);

        order.setStatus("processing");
        orderRepository.save(order);

        return paymentMapper.toDto(payment);
    }

    private String normalizeFulfillmentMethod(String fulfillmentMethod) {
        if (fulfillmentMethod == null || fulfillmentMethod.trim().isEmpty()) {
            throw new BadRequestException("Fulfillment method is required");
        }
        String normalized = fulfillmentMethod.trim().toLowerCase();
        if ("delivery".equals(normalized) || "home_delivery".equals(normalized)) {
            return "home_delivery";
        }
        if ("pickup".equals(normalized)) {
            return "pickup";
        }
        throw new BadRequestException("Invalid fulfillment method. Allowed values: home_delivery, pickup");
    }

    private String normalizePaymentMethod(String paymentMethod) {
        if (paymentMethod == null || paymentMethod.trim().isEmpty()) {
            throw new BadRequestException("Payment method is required");
        }
        String normalized = paymentMethod.trim().toLowerCase();
        if ("cash".equals(normalized) || "cod".equals(normalized)) {
            return "cod";
        }
        if ("card".equals(normalized) || "bank_transfer".equals(normalized)) {
            return normalized;
        }
        throw new BadRequestException("Invalid payment method. Allowed values: cod, card, bank_transfer");
    }
}
