package com.badran.store.coupon.mapper;

import com.badran.store.coupon.dto.CouponDto;
import com.badran.store.coupon.entity.Coupon;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-07-08T15:46:00+0300",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.10 (Homebrew)"
)
@Component
public class CouponMapperImpl implements CouponMapper {

    @Override
    public CouponDto toDto(Coupon coupon) {
        if ( coupon == null ) {
            return null;
        }

        CouponDto couponDto = new CouponDto();

        couponDto.setCouponId( coupon.getCouponId() );
        couponDto.setCode( coupon.getCode() );
        couponDto.setType( coupon.getType() );
        couponDto.setValue( coupon.getValue() );
        couponDto.setValidFrom( coupon.getValidFrom() );
        couponDto.setValidTo( coupon.getValidTo() );
        couponDto.setIsActive( coupon.getIsActive() );

        return couponDto;
    }
}
