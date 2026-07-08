package com.badran.store.review.dto;

import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class ReviewDto {
    private Long reviewId;
    private Long productId;
    private Long userId;
    private Long orderId;
    private Integer rating;
    private String comment;
    private String status;
    private OffsetDateTime createdAt;
}
