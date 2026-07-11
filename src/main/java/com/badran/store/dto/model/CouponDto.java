package com.badran.store.dto.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Coupon response used when an order has an applied discount.
 */
@Data
@Schema(description = "Discount coupon details.")
public class CouponDto {
    @Schema(description = "Coupon identifier.", example = "12")
    private Long couponId;

    @Schema(description = "Coupon code.", example = "WELCOME10")
    private String code;

    @Schema(description = "Coupon discount type.", example = "percentage")
    private String type;

    @Schema(description = "Coupon value.", example = "10.00")
    private BigDecimal value;

    @Schema(description = "First valid date.", example = "2026-01-01")
    private LocalDate validFrom;

    @Schema(description = "Last valid date.", example = "2026-12-31")
    private LocalDate validTo;

    @Schema(description = "Whether the coupon is active.", example = "true")
    private Boolean isActive;
}
