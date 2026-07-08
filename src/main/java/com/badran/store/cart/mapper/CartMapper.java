package com.badran.store.cart.mapper;

import com.badran.store.cart.dto.CartDto;
import com.badran.store.cart.dto.CartItemDto;
import com.badran.store.cart.entity.Cart;
import com.badran.store.cart.entity.CartItem;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CartMapper {
    CartDto toDto(Cart cart);
    CartItemDto toDto(CartItem item);
}
