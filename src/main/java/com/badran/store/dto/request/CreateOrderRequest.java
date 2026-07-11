package com.badran.store.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * Checkout payload used to convert a cart into an order.
 */
@Data
@Schema(description = "Checkout details used to create an order from the current cart.")
public class CreateOrderRequest {

    @NotBlank(message = "Fulfillment method is required")
    @Size(max = 30, message = "Fulfillment method must not exceed 30 characters")
    @Schema(description = "Fulfillment method. Accepted values are home_delivery or pickup.", example = "home_delivery")
    private String fulfillmentMethod; // home_delivery, pickup

    @Size(max = 255, message = "Guest name must not exceed 255 characters")
    @Schema(description = "Guest customer name for anonymous checkout.", example = "Ahmad Khalil")
    private String guestName;

    @Size(max = 255, message = "Guest phone must not exceed 255 characters")
    @Schema(description = "Guest customer phone for anonymous checkout.", example = "+970599123456")
    private String guestPhone;

    @Email(message = "Invalid guest email format")
    @Size(max = 255, message = "Guest email must not exceed 255 characters")
    @Schema(description = "Guest customer email for anonymous checkout.", example = "ahmad.khalil@example.com")
    private String guestEmail;

    @Positive(message = "Delivery address ID must be greater than zero")
    @Schema(description = "Saved delivery address identifier for authenticated users.", example = "7")
    private Long deliveryAddressId;

    @Size(max = 255, message = "Delivery city must not exceed 255 characters")
    @Schema(description = "Delivery city.", example = "Ramallah")
    private String deliveryCity;

    @Size(max = 255, message = "Delivery zone must not exceed 255 characters")
    @Schema(description = "Delivery zone or neighborhood.", example = "Al-Tireh")
    private String deliveryZone;

    @Size(max = 255, message = "Delivery address line must not exceed 255 characters")
    @Schema(description = "Delivery street and building details.", example = "Main Street, Building 12")
    private String deliveryAddressLine;

    @Size(max = 255, message = "Coupon code must not exceed 255 characters")
    @Schema(description = "Optional coupon code.", example = "WELCOME10")
    private String couponCode;

    @NotBlank(message = "Payment method is required")
    @Size(max = 30, message = "Payment method must not exceed 30 characters")
    @Schema(description = "Payment method. Accepted values are cod, card, or bank_transfer.", example = "cod")
    private String paymentMethod; // cod, card, bank_transfer
}
