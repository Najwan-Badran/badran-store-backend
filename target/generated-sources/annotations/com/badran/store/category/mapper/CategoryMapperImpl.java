package com.badran.store.category.mapper;

import com.badran.store.category.dto.CategoryDto;
import com.badran.store.category.entity.Category;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-07-08T15:46:00+0300",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.10 (Homebrew)"
)
@Component
public class CategoryMapperImpl implements CategoryMapper {

    @Override
    public CategoryDto toDto(Category category) {
        if ( category == null ) {
            return null;
        }

        CategoryDto categoryDto = new CategoryDto();

        categoryDto.setParentCategoryId( categoryParentCategoryCategoryId( category ) );
        categoryDto.setCategoryId( category.getCategoryId() );
        categoryDto.setNameAr( category.getNameAr() );
        categoryDto.setNameEn( category.getNameEn() );

        return categoryDto;
    }

    private Long categoryParentCategoryCategoryId(Category category) {
        Category parentCategory = category.getParentCategory();
        if ( parentCategory == null ) {
            return null;
        }
        return parentCategory.getCategoryId();
    }
}
