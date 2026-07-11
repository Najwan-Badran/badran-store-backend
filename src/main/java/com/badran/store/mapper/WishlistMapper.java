package com.badran.store.mapper;

import com.badran.store.dto.model.WishlistDto;
import com.badran.store.entity.Wishlist;
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
