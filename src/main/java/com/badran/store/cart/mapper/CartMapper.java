package com.badran.store.cart.mapper;

import com.badran.store.cart.dto.CartDto;
import com.badran.store.cart.dto.CartItemDto;
import com.badran.store.cart.entity.Cart;
import com.badran.store.cart.entity.CartItem;
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
