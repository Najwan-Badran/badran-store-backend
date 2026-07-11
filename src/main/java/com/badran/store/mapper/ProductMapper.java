package com.badran.store.mapper;

import com.badran.store.dto.model.ProductDto;
import com.badran.store.dto.model.ProductImageDto;
import com.badran.store.entity.Product;
import com.badran.store.entity.ProductImage;
import org.mapstruct.Mapper;

/**
 * MapStruct mapper for converting product catalog entities and images into API DTOs.
 */
@Mapper(componentModel = "spring", uses = {CategoryMapper.class, BrandMapper.class})
public interface ProductMapper {
    /**
     * Converts a product entity into a catalog response DTO.
     */
    ProductDto toDto(Product product);

    /**
     * Converts a product image entity into a response DTO.
     */
    ProductImageDto toDto(ProductImage image);
}
