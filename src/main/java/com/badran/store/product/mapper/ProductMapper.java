package com.badran.store.product.mapper;

import com.badran.store.brand.mapper.BrandMapper;
import com.badran.store.category.mapper.CategoryMapper;
import com.badran.store.product.dto.ProductDto;
import com.badran.store.product.dto.ProductImageDto;
import com.badran.store.product.entity.Product;
import com.badran.store.product.entity.ProductImage;
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
