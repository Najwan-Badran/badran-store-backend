package com.badran.store.category.dto;

import lombok.Data;

@Data
public class CategoryDto {
    private Long categoryId;
    private String nameAr;
    private String nameEn;
    private Long parentCategoryId;
}
