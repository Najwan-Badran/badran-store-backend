package com.badran.store.brand.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * Brand response used to describe product manufacturer or supplier identity.
 */
@Data
@Schema(description = "Product brand information.")
public class BrandDto {
    @Schema(description = "Brand identifier.", example = "4")
    private Long brandId;

    @Schema(description = "Arabic brand name.", example = "باداران")
    private String nameAr;

    @Schema(description = "English brand name.", example = "Badran")
    private String nameEn;

    @Schema(description = "Brand logo URL.", example = "https://cdn.example.com/brands/badran.png")
    private String logoUrl;
}
