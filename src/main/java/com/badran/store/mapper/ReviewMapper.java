package com.badran.store.mapper;

import com.badran.store.dto.model.ReviewDto;
import com.badran.store.entity.Review;
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
