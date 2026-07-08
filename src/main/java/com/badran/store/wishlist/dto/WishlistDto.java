package com.badran.store.wishlist.dto;

import com.badran.store.product.dto.ProductDto;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class WishlistDto {
    private Long wishlistId;
    private Long userId;
    private ProductDto product;
    private OffsetDateTime addedAt;
}
