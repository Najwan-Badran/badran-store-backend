package com.badran.store.order.mapper;

import com.badran.store.order.dto.PaymentDto;
import com.badran.store.order.entity.Order;
import com.badran.store.order.entity.Payment;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-07-08T15:46:00+0300",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.10 (Homebrew)"
)
@Component
public class PaymentMapperImpl implements PaymentMapper {

    @Override
    public PaymentDto toDto(Payment payment) {
        if ( payment == null ) {
            return null;
        }

        PaymentDto paymentDto = new PaymentDto();

        paymentDto.setOrderId( paymentOrderOrderId( payment ) );
        paymentDto.setPaymentId( payment.getPaymentId() );
        paymentDto.setMethod( payment.getMethod() );
        paymentDto.setStatus( payment.getStatus() );
        paymentDto.setTransactionRef( payment.getTransactionRef() );
        paymentDto.setReceiptUrl( payment.getReceiptUrl() );
        paymentDto.setVerifiedByUserId( payment.getVerifiedByUserId() );
        paymentDto.setVerifiedAt( payment.getVerifiedAt() );
        paymentDto.setCreatedAt( payment.getCreatedAt() );

        return paymentDto;
    }

    private Long paymentOrderOrderId(Payment payment) {
        Order order = payment.getOrder();
        if ( order == null ) {
            return null;
        }
        return order.getOrderId();
    }
}
