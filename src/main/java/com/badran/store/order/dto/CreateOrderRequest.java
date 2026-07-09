package com.badran.store.order.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * Checkout payload used to convert a cart into an order.
 */
@Data
@Schema(description = "Checkout details used to create an order from the current cart.")
public class CreateOrderRequest {

    @NotBlank(message = "Fulfillment method is required")
    @Schema(description = "Fulfillment method. Accepted values are home_delivery or pickup.", example = "home_delivery")
    private String fulfillmentMethod; // home_delivery, pickup

    // Guest checkout details (optional if authenticated)
    @Schema(description = "Guest customer name for anonymous checkout.", example = "Ahmad Khalil")
    private String guestName;

    @Schema(description = "Guest customer phone for anonymous checkout.", example = "+970599123456")
    private String guestPhone;

    @Schema(description = "Guest customer email for anonymous checkout.", example = "ahmad.khalil@example.com")
    private String guestEmail;

    // Delivery address details
    @Schema(description = "Saved delivery address identifier for authenticated users.", example = "7")
    private Long deliveryAddressId;

    @Schema(description = "Delivery city.", example = "Ramallah")
    private String deliveryCity;

    @Schema(description = "Delivery zone or neighborhood.", example = "Al-Tireh")
    private String deliveryZone;

    @Schema(description = "Delivery street and building details.", example = "Main Street, Building 12")
    private String deliveryAddressLine;

    @Schema(description = "Optional coupon code.", example = "WELCOME10")
    private String couponCode;

    @NotBlank(message = "Payment method is required")
    @Schema(description = "Payment method. Accepted values are cod, card, or bank_transfer.", example = "cod")
    private String paymentMethod; // cod, card, bank_transfer
}
