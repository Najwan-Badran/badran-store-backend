package com.badran.store.dto.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * Cart item response representing one product line in a cart.
 */
@Data
@Schema(description = "One product line in a shopping cart.")
public class CartItemDto {
    @Schema(description = "Cart item identifier.", example = "10")
    private Long cartItemId;

    @Schema(description = "Product identifier.", example = "101")
    private Long productId;

    @Schema(description = "Quantity in the cart.", example = "2")
    private Integer quantity;
}
