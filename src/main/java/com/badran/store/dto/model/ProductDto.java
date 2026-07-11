package com.badran.store.dto.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Product catalog response used by listing, detail, wishlist, and review workflows.
 */
@Data
@Schema(description = "Product catalog item with pricing, inventory, localized text, category, brand, and media.")
public class ProductDto {
    @Schema(description = "Product identifier.", example = "101")
    private Long productId;

    @Schema(description = "Stock keeping unit.", example = "CW-SHAMPOO-001")
    private String sku;

    @Schema(description = "Arabic product name.", example = "شامبو سيارات فاخر")
    private String nameAr;

    @Schema(description = "English product name.", example = "Premium Car Shampoo")
    private String nameEn;

    @Schema(description = "Arabic product description.", example = "شامبو مركز لتنظيف السيارات")
    private String descriptionAr;

    @Schema(description = "English product description.", example = "Concentrated shampoo for exterior car cleaning.")
    private String descriptionEn;

    @Schema(description = "Product category.")
    private CategoryDto category;

    @Schema(description = "Product brand.")
    private BrandDto brand;

    @Schema(description = "Base unit price.", example = "35.00")
    private BigDecimal basePrice;

    @Schema(description = "Available stock quantity.", example = "75")
    private Integer stockQuantity;

    @Schema(description = "Stock level that should trigger reorder attention.", example = "10")
    private Integer reorderThreshold;

    @Schema(description = "Whether the product is visible in the active catalog.", example = "true")
    private Boolean isActive;

    @Schema(description = "Whether the product is marked as on sale.", example = "false")
    private Boolean isOnSale;

    @Schema(description = "Whether the product is marked as a new arrival.", example = "true")
    private Boolean isNewArrival;

    @Schema(description = "Average customer rating.", example = "4.50")
    private BigDecimal avgRating;

    @Schema(description = "Number of published reviews.", example = "12")
    private Integer reviewCount;

    @Schema(description = "Flexible product specifications stored as JSON.", example = "{\"volume\":\"1L\",\"scent\":\"citrus\"}")
    private Map<String, Object> specifications;

    @Schema(description = "Product images ordered for display.")
    private List<ProductImageDto> images;
}
