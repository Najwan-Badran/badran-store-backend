package com.badran.store.product.mapper;

import com.badran.store.brand.mapper.BrandMapper;
import com.badran.store.category.mapper.CategoryMapper;
import com.badran.store.product.dto.ProductDto;
import com.badran.store.product.dto.ProductImageDto;
import com.badran.store.product.entity.Product;
import com.badran.store.product.entity.ProductImage;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {CategoryMapper.class, BrandMapper.class})
public interface ProductMapper {
    ProductDto toDto(Product product);
    ProductImageDto toDto(ProductImage image);
}
