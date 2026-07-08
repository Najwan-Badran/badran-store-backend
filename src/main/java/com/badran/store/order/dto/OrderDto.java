package com.badran.store.order.dto;

import com.badran.store.coupon.dto.CouponDto;
import lombok.Data;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Data
public class OrderDto {
    private Long orderId;
    private UUID publicId;
    private String orderNumber;
    private Long userId;
    private String guestName;
    private String guestPhone;
    private String guestEmail;
    private String fulfillmentMethod;
    private Long deliveryAddressId;
    private String deliveryCity;
    private String deliveryZone;
    private String deliveryAddressLine;
    private BigDecimal deliveryFee;
    private String status;
    private BigDecimal subtotal;
    private CouponDto coupon;
    private BigDecimal discountAmount;
    private BigDecimal total;
    private OffsetDateTime createdAt;
    private List<OrderItemDto> items;
}
