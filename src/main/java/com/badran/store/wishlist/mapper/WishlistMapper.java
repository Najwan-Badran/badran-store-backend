package com.badran.store.wishlist.mapper;

import com.badran.store.product.mapper.ProductMapper;
import com.badran.store.wishlist.dto.WishlistDto;
import com.badran.store.wishlist.entity.Wishlist;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {ProductMapper.class})
public interface WishlistMapper {
    WishlistDto toDto(Wishlist wishlist);
}
