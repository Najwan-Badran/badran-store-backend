package com.badran.store.dto.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * Product image response for catalog media.
 */
@Data
@Schema(description = "Image metadata for a product.")
public class ProductImageDto {
    @Schema(description = "Image identifier.", example = "501")
    private Long imageId;

    @Schema(description = "Public image URL.", example = "https://cdn.example.com/products/cw-shampoo-001.jpg")
    private String url;

    @Schema(description = "Display order for product images.", example = "1")
    private Integer sortOrder;
}
