package com.badran.store.dto.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Order item response representing one purchased product line.
 */
@Data
@Schema(description = "One product line in an order.")
public class OrderItemDto {
    @Schema(description = "Order item identifier.", example = "9001")
    private Long orderItemId;

    @Schema(description = "Product identifier.", example = "101")
    private Long productId;

    @Schema(description = "Purchased quantity.", example = "2")
    private Integer quantity;

    @Schema(description = "Unit price captured at checkout.", example = "60.00")
    private BigDecimal unitPrice;

    @Schema(description = "Quantity multiplied by unit price.", example = "120.00")
    private BigDecimal lineTotal;
}
