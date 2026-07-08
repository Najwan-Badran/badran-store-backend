package com.badran.store.coupon.mapper;

import com.badran.store.coupon.dto.CouponDto;
import com.badran.store.coupon.entity.Coupon;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CouponMapper {
    CouponDto toDto(Coupon coupon);
}
