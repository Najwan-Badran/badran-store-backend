package com.badran.store.coupon.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class CouponDto {
    private Long couponId;
    private String code;
    private String type;
    private BigDecimal value;
    private LocalDate validFrom;
    private LocalDate validTo;
    private Boolean isActive;
}
