package com.badran.store.dto.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * Category response used to classify catalog products.
 */
@Data
@Schema(description = "Product category information.")
public class CategoryDto {
    @Schema(description = "Category identifier.", example = "8")
    private Long categoryId;

    @Schema(description = "Arabic category name.", example = "مستلزمات الغسيل")
    private String nameAr;

    @Schema(description = "English category name.", example = "Washing Supplies")
    private String nameEn;

    @Schema(description = "Parent category identifier when this category is nested.", example = "2")
    private Long parentCategoryId;
}
