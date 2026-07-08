package com.badran.store.brand.mapper;

import com.badran.store.brand.dto.BrandDto;
import com.badran.store.brand.entity.Brand;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BrandMapper {
    BrandDto toDto(Brand brand);

    @Mapping(target = "createdAt", ignore = true)
    Brand toEntity(BrandDto dto);
}
