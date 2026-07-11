package com.badran.store.service.impl;

import com.badran.store.exception.BadRequestException;
import com.badran.store.dto.model.ProductDto;
import com.badran.store.entity.Product;
import com.badran.store.mapper.ProductMapper;
import com.badran.store.repository.ProductRepository;
import com.badran.store.mapper.ReviewMapper;
import com.badran.store.repository.ReviewRepository;
import com.badran.store.mapper.WishlistMapper;
import com.badran.store.repository.WishlistRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    ProductRepository productRepository;
    @Mock
    WishlistRepository wishlistRepository;
    @Mock
    ReviewRepository reviewRepository;
    @Mock
    ProductMapper productMapper;
    @Mock
    WishlistMapper wishlistMapper;
    @Mock
    ReviewMapper reviewMapper;

    @InjectMocks
    ProductServiceImpl productService;

    @Test
    void verifyAndDeductStockRejectsNonPositiveQuantityBeforeDatabaseWrite() {
        assertThatThrownBy(() -> productService.verifyAndDeductStock(1L, 0))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("greater than zero");

        verify(productRepository, never()).save(org.mockito.ArgumentMatchers.any());
    }

    @Test
    void verifyAndDeductStockUsesLockedLookupAndPersistsDeduction() {
        Product product = Product.builder()
                .productId(1L)
                .sku("SKU-1")
                .stockQuantity(10)
                .basePrice(BigDecimal.TEN)
                .build();
        ProductDto dto = new ProductDto();
        dto.setProductId(1L);
        dto.setStockQuantity(7);

        when(productRepository.findByIdForUpdate(1L)).thenReturn(Optional.of(product));
        when(productRepository.save(product)).thenReturn(product);
        when(productMapper.toDto(product)).thenReturn(dto);

        ProductDto result = productService.verifyAndDeductStock(1L, 3);

        assertThat(product.getStockQuantity()).isEqualTo(7);
        assertThat(result.getStockQuantity()).isEqualTo(7);
        verify(productRepository).findByIdForUpdate(1L);
        verify(productRepository).save(product);
    }
}
