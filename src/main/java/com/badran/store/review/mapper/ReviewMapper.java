package com.badran.store.review.mapper;

import com.badran.store.review.dto.ReviewDto;
import com.badran.store.review.entity.Review;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * MapStruct mapper for product review API responses.
 */
@Mapper(componentModel = "spring")
public interface ReviewMapper {
    /**
     * Converts a review entity into a response DTO with product id flattened.
     */
    @Mapping(target = "productId", source = "product.productId")
    ReviewDto toDto(Review review);
}
