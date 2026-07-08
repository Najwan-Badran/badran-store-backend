package com.badran.store.brand.mapper;

import com.badran.store.brand.dto.BrandDto;
import com.badran.store.brand.entity.Brand;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-07-08T15:46:00+0300",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.10 (Homebrew)"
)
@Component
public class BrandMapperImpl implements BrandMapper {

    @Override
    public BrandDto toDto(Brand brand) {
        if ( brand == null ) {
            return null;
        }

        BrandDto brandDto = new BrandDto();

        brandDto.setBrandId( brand.getBrandId() );
        brandDto.setNameAr( brand.getNameAr() );
        brandDto.setNameEn( brand.getNameEn() );
        brandDto.setLogoUrl( brand.getLogoUrl() );

        return brandDto;
    }

    @Override
    public Brand toEntity(BrandDto dto) {
        if ( dto == null ) {
            return null;
        }

        Brand.BrandBuilder brand = Brand.builder();

        brand.brandId( dto.getBrandId() );
        brand.nameAr( dto.getNameAr() );
        brand.nameEn( dto.getNameEn() );
        brand.logoUrl( dto.getLogoUrl() );

        return brand.build();
    }
}
