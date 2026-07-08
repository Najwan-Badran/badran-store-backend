package com.badran.store.category.mapper;

import com.badran.store.category.dto.CategoryDto;
import com.badran.store.category.entity.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    @Mapping(target = "parentCategoryId", source = "parentCategory.categoryId")
    CategoryDto toDto(Category category);
}
