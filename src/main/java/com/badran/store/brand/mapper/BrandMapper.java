package com.badran.store.brand.mapper;

import com.badran.store.brand.dto.BrandDto;
import com.badran.store.brand.entity.Brand;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * MapStruct mapper for converting brand entities to API DTOs and back.
 */
@Mapper(componentModel = "spring")
public interface BrandMapper {
    /**
     * Converts a brand entity into an API response DTO.
     */
    BrandDto toDto(Brand brand);

    /**
     * Converts a brand DTO into an entity for persistence workflows.
     */
    @Mapping(target = "createdAt", ignore = true)
    Brand toEntity(BrandDto dto);
}
