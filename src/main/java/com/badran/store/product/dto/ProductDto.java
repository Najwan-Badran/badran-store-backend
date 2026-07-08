package com.badran.store.product.dto;

import com.badran.store.brand.dto.BrandDto;
import com.badran.store.category.dto.CategoryDto;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Data
public class ProductDto {
    private Long productId;
    private String sku;
    private String nameAr;
    private String nameEn;
    private String descriptionAr;
    private String descriptionEn;
    private CategoryDto category;
    private BrandDto brand;
    private BigDecimal basePrice;
    private Integer stockQuantity;
    private Integer reorderThreshold;
    private Boolean isActive;
    private Boolean isOnSale;
    private Boolean isNewArrival;
    private BigDecimal avgRating;
    private Integer reviewCount;
    private Map<String, Object> specifications;
    private List<ProductImageDto> images;
}
