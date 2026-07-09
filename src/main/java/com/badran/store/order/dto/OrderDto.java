package com.badran.store.order.dto;

import com.badran.store.coupon.dto.CouponDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Order response returned for checkout, lookup, and history operations.
 */
@Data
@Schema(description = "Order details including totals, fulfillment information, coupon, and item lines.")
public class OrderDto {
    @Schema(description = "Order database identifier.", example = "5001")
    private Long orderId;

    @Schema(description = "Public UUID for external order references.", example = "550e8400-e29b-41d4-a716-446655440000")
    private UUID publicId;

    @Schema(description = "Human-readable order number.", example = "ORD-20260708-0001")
    private String orderNumber;

    @Schema(description = "Owner user identifier for authenticated orders.", example = "2")
    private Long userId;

    @Schema(description = "Guest customer name for anonymous orders.", example = "Ahmad Khalil")
    private String guestName;

    @Schema(description = "Guest phone number.", example = "+970599123456")
    private String guestPhone;

    @Schema(description = "Guest email address.", example = "ahmad.khalil@example.com")
    private String guestEmail;

    @Schema(description = "Fulfillment method.", example = "home_delivery")
    private String fulfillmentMethod;

    @Schema(description = "Saved delivery address identifier.", example = "7")
    private Long deliveryAddressId;

    @Schema(description = "Delivery city.", example = "Ramallah")
    private String deliveryCity;

    @Schema(description = "Delivery zone.", example = "Al-Tireh")
    private String deliveryZone;

    @Schema(description = "Delivery address line.", example = "Main Street, Building 12")
    private String deliveryAddressLine;

    @Schema(description = "Delivery fee applied to the order.", example = "10.00")
    private BigDecimal deliveryFee;

    @Schema(description = "Current order status.", example = "pending")
    private String status;

    @Schema(description = "Subtotal before discounts and delivery.", example = "120.00")
    private BigDecimal subtotal;

    @Schema(description = "Applied coupon details, when present.")
    private CouponDto coupon;

    @Schema(description = "Discount amount applied to the order.", example = "12.00")
    private BigDecimal discountAmount;

    @Schema(description = "Final order total.", example = "118.00")
    private BigDecimal total;

    @Schema(description = "Order creation timestamp.", example = "2026-07-08T20:15:30+03:00")
    private OffsetDateTime createdAt;

    @Schema(description = "Order item lines.")
    private List<OrderItemDto> items;
}
