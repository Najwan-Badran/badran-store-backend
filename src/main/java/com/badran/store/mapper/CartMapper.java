package com.badran.store.mapper;

import com.badran.store.dto.model.CartDto;
import com.badran.store.dto.model.CartItemDto;
import com.badran.store.entity.Cart;
import com.badran.store.entity.CartItem;
import org.mapstruct.Mapper;

/**
 * MapStruct mapper for converting cart aggregates and item lines into API DTOs.
 */
@Mapper(componentModel = "spring")
public interface CartMapper {
    /**
     * Converts a cart aggregate into a response DTO.
     */
    CartDto toDto(Cart cart);

    /**
     * Converts a cart item entity into a response DTO.
     */
    CartItemDto toDto(CartItem item);
}
