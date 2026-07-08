package com.badran.store.product.mapper;

import com.badran.store.brand.mapper.BrandMapper;
import com.badran.store.category.mapper.CategoryMapper;
import com.badran.store.product.dto.ProductDto;
import com.badran.store.product.dto.ProductImageDto;
import com.badran.store.product.entity.Product;
import com.badran.store.product.entity.ProductImage;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-07-08T15:46:00+0300",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.10 (Homebrew)"
)
@Component
public class ProductMapperImpl implements ProductMapper {

    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private BrandMapper brandMapper;

    @Override
    public ProductDto toDto(Product product) {
        if ( product == null ) {
            return null;
        }

        ProductDto productDto = new ProductDto();

        productDto.setProductId( product.getProductId() );
        productDto.setSku( product.getSku() );
        productDto.setNameAr( product.getNameAr() );
        productDto.setNameEn( product.getNameEn() );
        productDto.setDescriptionAr( product.getDescriptionAr() );
        productDto.setDescriptionEn( product.getDescriptionEn() );
        productDto.setCategory( categoryMapper.toDto( product.getCategory() ) );
        productDto.setBrand( brandMapper.toDto( product.getBrand() ) );
        productDto.setBasePrice( product.getBasePrice() );
        productDto.setStockQuantity( product.getStockQuantity() );
        productDto.setReorderThreshold( product.getReorderThreshold() );
        productDto.setIsActive( product.getIsActive() );
        productDto.setIsOnSale( product.getIsOnSale() );
        productDto.setIsNewArrival( product.getIsNewArrival() );
        productDto.setAvgRating( product.getAvgRating() );
        productDto.setReviewCount( product.getReviewCount() );
        Map<String, Object> map = product.getSpecifications();
        if ( map != null ) {
            productDto.setSpecifications( new LinkedHashMap<String, Object>( map ) );
        }
        productDto.setImages( productImageListToProductImageDtoList( product.getImages() ) );

        return productDto;
    }

    @Override
    public ProductImageDto toDto(ProductImage image) {
        if ( image == null ) {
            return null;
        }

        ProductImageDto productImageDto = new ProductImageDto();

        productImageDto.setImageId( image.getImageId() );
        productImageDto.setUrl( image.getUrl() );
        productImageDto.setSortOrder( image.getSortOrder() );

        return productImageDto;
    }

    protected List<ProductImageDto> productImageListToProductImageDtoList(List<ProductImage> list) {
        if ( list == null ) {
            return null;
        }

        List<ProductImageDto> list1 = new ArrayList<ProductImageDto>( list.size() );
        for ( ProductImage productImage : list ) {
            list1.add( toDto( productImage ) );
        }

        return list1;
    }
}
