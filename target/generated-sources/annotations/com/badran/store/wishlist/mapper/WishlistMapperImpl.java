package com.badran.store.wishlist.mapper;

import com.badran.store.product.mapper.ProductMapper;
import com.badran.store.wishlist.dto.WishlistDto;
import com.badran.store.wishlist.entity.Wishlist;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-07-08T15:46:00+0300",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.10 (Homebrew)"
)
@Component
public class WishlistMapperImpl implements WishlistMapper {

    @Autowired
    private ProductMapper productMapper;

    @Override
    public WishlistDto toDto(Wishlist wishlist) {
        if ( wishlist == null ) {
            return null;
        }

        WishlistDto wishlistDto = new WishlistDto();

        wishlistDto.setWishlistId( wishlist.getWishlistId() );
        wishlistDto.setUserId( wishlist.getUserId() );
        wishlistDto.setProduct( productMapper.toDto( wishlist.getProduct() ) );
        wishlistDto.setAddedAt( wishlist.getAddedAt() );

        return wishlistDto;
    }
}
