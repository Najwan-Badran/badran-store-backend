package com.badran.store.dto.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.OffsetDateTime;

/**
 * Wishlist response representing one saved product for a user.
 */
@Data
@Schema(description = "Saved wishlist product entry.")
public class WishlistDto {
    @Schema(description = "Wishlist entry identifier.", example = "7001")
    private Long wishlistId;

    @Schema(description = "Owning user identifier.", example = "2")
    private Long userId;

    @Schema(description = "Saved product details.")
    private ProductDto product;

    @Schema(description = "Timestamp when the product was added.", example = "2026-07-08T20:35:00+03:00")
    private OffsetDateTime addedAt;
}
