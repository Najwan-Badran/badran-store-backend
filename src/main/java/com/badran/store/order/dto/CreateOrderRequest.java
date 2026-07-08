package com.badran.store.order.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateOrderRequest {

    @NotBlank(message = "Fulfillment method is required")
    private String fulfillmentMethod; // home_delivery, pickup

    // Guest checkout details (optional if authenticated)
    private String guestName;
    private String guestPhone;
    private String guestEmail;

    // Delivery address details
    private Long deliveryAddressId;
    private String deliveryCity;
    private String deliveryZone;
    private String deliveryAddressLine;

    private String couponCode;

    @NotBlank(message = "Payment method is required")
    private String paymentMethod; // cod, card, bank_transfer
}
