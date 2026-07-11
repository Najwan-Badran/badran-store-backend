package com.badran.store.mapper;

import com.badran.store.dto.model.CouponDto;
import com.badran.store.entity.Coupon;
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
