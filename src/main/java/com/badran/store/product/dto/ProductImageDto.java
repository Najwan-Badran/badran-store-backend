package com.badran.store.product.dto;

import lombok.Data;

@Data
public class ProductImageDto {
    private Long imageId;
    private String url;
    private Integer sortOrder;
}
