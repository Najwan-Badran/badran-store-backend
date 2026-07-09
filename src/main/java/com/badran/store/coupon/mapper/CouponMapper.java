package com.badran.store.coupon.mapper;

import com.badran.store.coupon.dto.CouponDto;
import com.badran.store.coupon.entity.Coupon;
import org.mapstruct.Mapper;

/**
 * MapStruct mapper for coupon discount details returned with orders.
 */
@Mapper(componentModel = "spring")
public interface CouponMapper {
    /**
     * Converts a coupon entity into a response DTO.
     */
    CouponDto toDto(Coupon coupon);
}
