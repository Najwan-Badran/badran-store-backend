package com.badran.store.cart.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

/**
 * Request payload for adding a product quantity to a cart.
 */
@Data
@Schema(description = "Product and quantity to add to the active cart.")
public class AddToCartRequest {
    @NotNull(message = "Product ID is required")
    @Schema(description = "Product identifier to add.", example = "101")
    private Long productId;

    @NotNull(message = "Quantity is required")
    @Positive(message = "Quantity must be greater than zero")
    @Schema(description = "Positive quantity to add.", example = "2")
    private Integer quantity;
}
