package com.badran.store.cart.mapper;

import com.badran.store.cart.dto.CartDto;
import com.badran.store.cart.dto.CartItemDto;
import com.badran.store.cart.entity.Cart;
import com.badran.store.cart.entity.CartItem;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-07-08T15:46:00+0300",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.10 (Homebrew)"
)
@Component
public class CartMapperImpl implements CartMapper {

    @Override
    public CartDto toDto(Cart cart) {
        if ( cart == null ) {
            return null;
        }

        CartDto cartDto = new CartDto();

        cartDto.setCartId( cart.getCartId() );
        cartDto.setUserId( cart.getUserId() );
        cartDto.setSessionId( cart.getSessionId() );
        cartDto.setItems( cartItemListToCartItemDtoList( cart.getItems() ) );

        return cartDto;
    }

    @Override
    public CartItemDto toDto(CartItem item) {
        if ( item == null ) {
            return null;
        }

        CartItemDto cartItemDto = new CartItemDto();

        cartItemDto.setCartItemId( item.getCartItemId() );
        cartItemDto.setProductId( item.getProductId() );
        cartItemDto.setQuantity( item.getQuantity() );

        return cartItemDto;
    }

    protected List<CartItemDto> cartItemListToCartItemDtoList(List<CartItem> list) {
        if ( list == null ) {
            return null;
        }

        List<CartItemDto> list1 = new ArrayList<CartItemDto>( list.size() );
        for ( CartItem cartItem : list ) {
            list1.add( toDto( cartItem ) );
        }

        return list1;
    }
}
