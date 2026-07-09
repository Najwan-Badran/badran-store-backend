package com.badran.store.product.service;

import com.badran.store.common.DomainConstants;
import com.badran.store.exception.BadRequestException;
import com.badran.store.exception.ResourceNotFoundException;
import com.badran.store.product.dto.ProductDto;
import com.badran.store.review.dto.ReviewDto;
import com.badran.store.wishlist.dto.WishlistDto;
import com.badran.store.product.entity.Product;
import com.badran.store.review.entity.Review;
import com.badran.store.wishlist.entity.Wishlist;
import com.badran.store.product.mapper.ProductMapper;
import com.badran.store.review.mapper.ReviewMapper;
import com.badran.store.wishlist.mapper.WishlistMapper;
import com.badran.store.product.repository.ProductRepository;
import com.badran.store.review.repository.ReviewRepository;
import com.badran.store.wishlist.repository.WishlistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Default product service implementation for catalog lookup, inventory, wishlist, and review workflows.
 */
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final WishlistRepository wishlistRepository;
    private final ReviewRepository reviewRepository;
    
    private final ProductMapper productMapper;
    private final WishlistMapper wishlistMapper;
    private final ReviewMapper reviewMapper;

    /** {@inheritDoc} */
    @Override
    @Transactional(readOnly = true)
    public Page<ProductDto> getProducts(Long categoryId, Long brandId, String query, Pageable pageable) {
        return productRepository.searchProducts(categoryId, brandId, query, pageable)
                .map(productMapper::toDto);
    }

    /** {@inheritDoc} */
    @Override
    @Transactional(readOnly = true)
    public ProductDto getProductById(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + productId));
        return productMapper.toDto(product);
    }

    /** {@inheritDoc} */
    @Override
    @Transactional
    public ProductDto verifyAndDeductStock(Long productId, Integer quantity) {
        if (quantity == null || quantity <= 0) {
            throw new BadRequestException("Quantity to deduct must be greater than zero");
        }

        Product product = productRepository.findByIdForUpdate(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + productId));

        if (product.getStockQuantity() < quantity) {
            throw new BadRequestException("Insufficient stock for product SKU " + product.getSku() + 
                    ". Requested: " + quantity + ", Available: " + product.getStockQuantity());
        }

        product.setStockQuantity(product.getStockQuantity() - quantity);
        Product savedProduct = productRepository.save(product);
        return productMapper.toDto(savedProduct);
    }

    /** {@inheritDoc} */
    @Override
    @Transactional(readOnly = true)
    public List<WishlistDto> getWishlist(Long userId) {
        return wishlistRepository.findByUserId(userId).stream()
                .map(wishlistMapper::toDto)
                .collect(Collectors.toList());
    }

    /** {@inheritDoc} */
    @Override
    @Transactional
    public void addToWishlist(Long userId, Long productId) {
        if (wishlistRepository.existsByUserIdAndProductProductId(userId, productId)) {
            throw new BadRequestException("Product is already in wishlist");
        }
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        Wishlist wishlist = Wishlist.builder()
                .userId(userId)
                .product(product)
                .build();
        wishlistRepository.save(wishlist);
    }

    /** {@inheritDoc} */
    @Override
    @Transactional
    public void removeFromWishlist(Long userId, Long productId) {
        if (!wishlistRepository.existsByUserIdAndProductProductId(userId, productId)) {
            throw new ResourceNotFoundException("Product not found in wishlist");
        }
        wishlistRepository.deleteByUserIdAndProductProductId(userId, productId);
    }

    /** {@inheritDoc} */
    @Override
    @Transactional(readOnly = true)
    public List<ReviewDto> getProductReviews(Long productId) {
        return reviewRepository.findByProductProductIdAndStatus(productId, DomainConstants.ReviewStatus.PUBLISHED).stream()
                .map(reviewMapper::toDto)
                .collect(Collectors.toList());
    }

    /** {@inheritDoc} */
    @Override
    @Transactional
    public ReviewDto addReview(Long userId, Long productId, Long orderId, Integer rating, String comment) {
        if (rating == null || rating < 1 || rating > 5) {
            throw new BadRequestException("Rating must be between 1 and 5");
        }

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        Review review = Review.builder()
                .product(product)
                .userId(userId)
                .orderId(orderId)
                .rating(rating.shortValue())
                .comment(comment)
                .status(DomainConstants.ReviewStatus.PUBLISHED) // Auto-publish for university project simplicity
                .build();

        Review savedReview = reviewRepository.save(review);
        return reviewMapper.toDto(savedReview);
    }
}
