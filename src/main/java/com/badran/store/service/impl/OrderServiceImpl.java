package com.badran.store.service.impl;

import com.badran.store.constants.DomainConstants;
import com.badran.store.dto.request.AddToCartRequest;
import com.badran.store.dto.model.CartDto;
import com.badran.store.dto.request.CreateOrderRequest;
import com.badran.store.dto.model.OrderDto;
import com.badran.store.dto.model.PaymentDto;
import com.badran.store.dto.model.ProductDto;
import com.badran.store.entity.Cart;
import com.badran.store.entity.CartItem;
import com.badran.store.entity.Coupon;
import com.badran.store.entity.Order;
import com.badran.store.entity.OrderItem;
import com.badran.store.entity.Payment;
import com.badran.store.enums.OrderStatus;
import com.badran.store.enums.PaymentStatus;
import com.badran.store.exception.BadRequestException;
import com.badran.store.exception.ResourceNotFoundException;
import com.badran.store.mapper.CartMapper;
import com.badran.store.mapper.OrderMapper;
import com.badran.store.mapper.PaymentMapper;
import com.badran.store.repository.CartItemRepository;
import com.badran.store.repository.CartRepository;
import com.badran.store.repository.CouponRepository;
import com.badran.store.repository.OrderItemRepository;
import com.badran.store.repository.OrderRepository;
import com.badran.store.repository.PaymentRepository;
import com.badran.store.service.interfaces.OrderService;
import com.badran.store.service.interfaces.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Default order service implementation for carts, checkout, order lookup, and payment completion.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {

    private static final List<String> PAYABLE_PAYMENT_STATUSES = List.of(
            PaymentStatus.UNPAID.value(),
            PaymentStatus.PENDING_VERIFICATION.value()
    );

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

    /** {@inheritDoc} */
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

    /** {@inheritDoc} */
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

    /** {@inheritDoc} */
    @Override
    @Transactional
    public CartDto removeItemFromCart(Long userId, String sessionId, Long productId) {
        Cart cart = getOrCreateCart(userId, sessionId);
        CartItem item = cartItemRepository.findByCartCartIdAndProductId(cart.getCartId(), productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found in cart"));

        cart.getItems().removeIf(cartItem -> Objects.equals(cartItem.getCartItemId(), item.getCartItemId()));
        cartItemRepository.delete(item);
        cartItemRepository.flush();
        return cartMapper.toDto(getOrCreateCart(userId, sessionId));
    }

    /** {@inheritDoc} */
    @Override
    @Transactional
    public void clearCart(Long userId, String sessionId) {
        Cart cart = getOrCreateCart(userId, sessionId);
        cart.getItems().clear();
        cartRepository.save(cart);
    }

    /** {@inheritDoc} */
    @Override
    @Transactional
    public OrderDto createOrder(Long userId, String sessionId, CreateOrderRequest request) {
        String fulfillmentMethod = normalizeFulfillmentMethod(request.getFulfillmentMethod());
        String paymentMethod = normalizePaymentMethod(request.getPaymentMethod());
        Cart cart = getOrCreateCart(userId, sessionId);
        if (cart.getItems().isEmpty()) {
            throw new BadRequestException("Cannot checkout an empty cart");
        }

        CartPricing cartPricing = priceAndValidateCart(cart);
        BigDecimal subtotal = cartPricing.subtotal();

        CouponApplication couponApplication = applyCoupon(request.getCouponCode(), subtotal);
        BigDecimal deliveryFee = calculateDeliveryFee(fulfillmentMethod);
        BigDecimal total = subtotal.subtract(couponApplication.discountAmount()).add(deliveryFee);
        if (total.compareTo(BigDecimal.ZERO) < 0) {
            total = BigDecimal.ZERO;
        }

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
                .status(OrderStatus.PENDING.value())
                .subtotal(subtotal)
                .coupon(couponApplication.coupon())
                .discountAmount(couponApplication.discountAmount())
                .total(total)
                .build();

        Order savedOrder = orderRepository.save(order);
        createOrderItems(savedOrder, cart, cartPricing);
        Payment payment = Payment.builder()
                .order(savedOrder)
                .method(paymentMethod)
                .status(PaymentStatus.UNPAID.value())
                .build();
        paymentRepository.save(payment);

        cartItemRepository.deleteAll(cart.getItems());
        cart.getItems().clear();
        cartRepository.save(cart);

        log.info("Created order id={} number={} userId={} total={}",
                savedOrder.getOrderId(), savedOrder.getOrderNumber(), userId, savedOrder.getTotal());
        return orderMapper.toDto(savedOrder);
    }

    /** {@inheritDoc} */
    @Override
    @Transactional(readOnly = true)
    public OrderDto getOrderById(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with ID: " + orderId));
        return orderMapper.toDto(order);
    }

    /** {@inheritDoc} */
    @Override
    @Transactional(readOnly = true)
    public List<OrderDto> getOrdersByUser(Long userId) {
        return orderRepository.findByUserId(userId).stream()
                .map(orderMapper::toDto)
                .collect(Collectors.toList());
    }

    /** {@inheritDoc} */
    @Override
    @Transactional
    public PaymentDto payForOrder(Long orderId, String paymentMethod) {
        String normalizedPaymentMethod = normalizePaymentMethod(paymentMethod);
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        Payment payment = paymentRepository.findFirstByOrderOrderIdAndStatusIn(orderId, PAYABLE_PAYMENT_STATUSES)
                .orElseThrow(() -> new BadRequestException("No pending payment found for this order"));

        payment.setMethod(normalizedPaymentMethod);
        payment.setStatus(PaymentStatus.PAID.value());
        payment.setTransactionRef("TXN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        payment.setVerifiedAt(OffsetDateTime.now());
        paymentRepository.save(payment);

        order.setStatus(OrderStatus.PROCESSING.value());
        orderRepository.save(order);

        log.info("Completed payment id={} orderId={} method={} transactionRef={}",
                payment.getPaymentId(), orderId, normalizedPaymentMethod, payment.getTransactionRef());
        return paymentMapper.toDto(payment);
    }

    private String normalizeFulfillmentMethod(String fulfillmentMethod) {
        if (fulfillmentMethod == null || fulfillmentMethod.trim().isEmpty()) {
            throw new BadRequestException("Fulfillment method is required");
        }
        String normalized = fulfillmentMethod.trim().toLowerCase();
        if (DomainConstants.FulfillmentMethod.DELIVERY_ALIAS.equals(normalized)
                || DomainConstants.FulfillmentMethod.HOME_DELIVERY.equals(normalized)) {
            return DomainConstants.FulfillmentMethod.HOME_DELIVERY;
        }
        if (DomainConstants.FulfillmentMethod.PICKUP.equals(normalized)) {
            return DomainConstants.FulfillmentMethod.PICKUP;
        }
        throw new BadRequestException("Invalid fulfillment method. Allowed values: home_delivery, pickup");
    }

    private String normalizePaymentMethod(String paymentMethod) {
        if (paymentMethod == null || paymentMethod.trim().isEmpty()) {
            throw new BadRequestException("Payment method is required");
        }
        String normalized = paymentMethod.trim().toLowerCase();
        if (DomainConstants.PaymentMethod.CASH_ALIAS.equals(normalized)
                || DomainConstants.PaymentMethod.COD.equals(normalized)) {
            return DomainConstants.PaymentMethod.COD;
        }
        if (DomainConstants.PaymentMethod.CARD.equals(normalized)
                || DomainConstants.PaymentMethod.BANK_TRANSFER.equals(normalized)) {
            return normalized;
        }
        throw new BadRequestException("Invalid payment method. Allowed values: cod, card, bank_transfer");
    }

    private CouponApplication applyCoupon(String couponCode, BigDecimal subtotal) {
        if (couponCode == null || couponCode.trim().isEmpty()) {
            return new CouponApplication(null, BigDecimal.ZERO);
        }

        Coupon coupon = couponRepository.findByCodeIgnoreCaseForUpdate(couponCode.trim())
                .orElseThrow(() -> new BadRequestException("Invalid coupon code"));

        if (!coupon.getIsActive() || coupon.getValidFrom().isAfter(LocalDate.now()) || coupon.getValidTo().isBefore(LocalDate.now())) {
            throw new BadRequestException("Coupon is expired or inactive");
        }

        if (coupon.getUsageLimit() != null && coupon.getUsageCount() >= coupon.getUsageLimit()) {
            throw new BadRequestException("Coupon usage limit exceeded");
        }

        BigDecimal discountAmount = calculateDiscountAmount(coupon, subtotal);
        coupon.setUsageCount(coupon.getUsageCount() + 1);
        couponRepository.save(coupon);
        return new CouponApplication(coupon, discountAmount);
    }

    private BigDecimal calculateDiscountAmount(Coupon coupon, BigDecimal subtotal) {
        if ("percentage".equalsIgnoreCase(coupon.getType())) {
            return subtotal.multiply(coupon.getValue().divide(BigDecimal.valueOf(100)));
        }
        if ("fixed_amount".equalsIgnoreCase(coupon.getType())) {
            return coupon.getValue();
        }
        return BigDecimal.ZERO;
    }

    private BigDecimal calculateDeliveryFee(String fulfillmentMethod) {
        if (DomainConstants.FulfillmentMethod.HOME_DELIVERY.equalsIgnoreCase(fulfillmentMethod)) {
            return BigDecimal.valueOf(10.0);
        }
        return BigDecimal.ZERO;
    }

    private void createOrderItems(Order savedOrder, Cart cart, CartPricing cartPricing) {
        for (CartItem item : cart.getItems()) {
            ProductDto product = cartPricing.productsById().get(item.getProductId());

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
    }

    private CartPricing priceAndValidateCart(Cart cart) {
        BigDecimal subtotal = BigDecimal.ZERO;
        Map<Long, ProductDto> productsById = new HashMap<>();

        for (CartItem item : cart.getItems()) {
            ProductDto product = productService.getProductById(item.getProductId());
            if (product.getStockQuantity() < item.getQuantity()) {
                throw new BadRequestException("Product " + product.getNameEn() + " has insufficient stock");
            }
            productsById.put(item.getProductId(), product);
            subtotal = subtotal.add(product.getBasePrice().multiply(BigDecimal.valueOf(item.getQuantity())));
        }

        return new CartPricing(subtotal, productsById);
    }

    private record CartPricing(BigDecimal subtotal, Map<Long, ProductDto> productsById) {
    }

    private record CouponApplication(Coupon coupon, BigDecimal discountAmount) {
    }
}
