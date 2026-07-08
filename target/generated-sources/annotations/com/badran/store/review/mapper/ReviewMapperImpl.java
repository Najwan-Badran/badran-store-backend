package com.badran.store.review.mapper;

import com.badran.store.product.entity.Product;
import com.badran.store.review.dto.ReviewDto;
import com.badran.store.review.entity.Review;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-07-08T15:46:00+0300",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.10 (Homebrew)"
)
@Component
public class ReviewMapperImpl implements ReviewMapper {

    @Override
    public ReviewDto toDto(Review review) {
        if ( review == null ) {
            return null;
        }

        ReviewDto reviewDto = new ReviewDto();

        reviewDto.setProductId( reviewProductProductId( review ) );
        reviewDto.setReviewId( review.getReviewId() );
        reviewDto.setUserId( review.getUserId() );
        reviewDto.setOrderId( review.getOrderId() );
        if ( review.getRating() != null ) {
            reviewDto.setRating( review.getRating().intValue() );
        }
        reviewDto.setComment( review.getComment() );
        reviewDto.setStatus( review.getStatus() );
        reviewDto.setCreatedAt( review.getCreatedAt() );

        return reviewDto;
    }

    private Long reviewProductProductId(Review review) {
        Product product = review.getProduct();
        if ( product == null ) {
            return null;
        }
        return product.getProductId();
    }
}
