package com.badran.store.review.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.OffsetDateTime;

/**
 * Review response representing a published customer product review.
 */
@Data
@Schema(description = "Customer review for a product and completed order.")
public class ReviewDto {
    @Schema(description = "Review identifier.", example = "2001")
    private Long reviewId;

    @Schema(description = "Reviewed product identifier.", example = "101")
    private Long productId;

    @Schema(description = "Reviewing user identifier.", example = "2")
    private Long userId;

    @Schema(description = "Order identifier proving purchase context.", example = "5001")
    private Long orderId;

    @Schema(description = "Rating from one to five.", example = "5")
    private Integer rating;

    @Schema(description = "Optional review comment.", example = "Excellent cleaning power and good scent.")
    private String comment;

    @Schema(description = "Review moderation status.", example = "published")
    private String status;

    @Schema(description = "Review creation timestamp.", example = "2026-07-08T20:25:00+03:00")
    private OffsetDateTime createdAt;
}
