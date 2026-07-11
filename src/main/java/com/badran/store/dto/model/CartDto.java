package com.badran.store.dto.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * Cart response containing cart identity and item lines.
 */
@Data
@Schema(description = "Shopping cart with user/session ownership and item lines.")
public class CartDto {
    @Schema(description = "Cart identifier.", example = "1")
    private Long cartId;

    @Schema(description = "Authenticated user identifier when the cart belongs to a user.", example = "2")
    private Long userId;

    @Schema(description = "Anonymous session identifier when the cart belongs to a guest.", example = "session-abc-123")
    private String sessionId;

    @Schema(description = "Cart item lines.")
    private List<CartItemDto> items;
}
