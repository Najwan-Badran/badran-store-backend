package com.badran.store.review.mapper;

import com.badran.store.review.dto.ReviewDto;
import com.badran.store.review.entity.Review;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ReviewMapper {
    @Mapping(target = "productId", source = "product.productId")
    ReviewDto toDto(Review review);
}
