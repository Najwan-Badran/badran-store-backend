package com.badran.store.mapper;

import com.badran.store.dto.model.CategoryDto;
import com.badran.store.entity.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * MapStruct mapper for converting category entities into localized API DTOs.
 */
@Mapper(componentModel = "spring")
public interface CategoryMapper {
    /**
     * Converts a category entity into a DTO with parent category id flattened.
     */
    @Mapping(target = "parentCategoryId", source = "parentCategory.categoryId")
    CategoryDto toDto(Category category);
}
