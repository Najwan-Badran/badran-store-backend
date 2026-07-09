package com.badran.store.wishlist.mapper;

import com.badran.store.product.mapper.ProductMapper;
import com.badran.store.wishlist.dto.WishlistDto;
import com.badran.store.wishlist.entity.Wishlist;
import org.mapstruct.Mapper;

/**
 * MapStruct mapper for wishlist entries and nested product details.
 */
@Mapper(componentModel = "spring", uses = {ProductMapper.class})
public interface WishlistMapper {
    /**
     * Converts a wishlist entry into a response DTO.
     */
    WishlistDto toDto(Wishlist wishlist);
}
