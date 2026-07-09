package com.badran.store.order.mapper;

import com.badran.store.order.dto.PaymentDto;
import com.badran.store.order.entity.Payment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * MapStruct mapper for converting payment entities into API DTOs.
 */
@Mapper(componentModel = "spring")
public interface PaymentMapper {
    /**
     * Converts a payment entity into a response DTO with the order id flattened.
     */
    @Mapping(target = "orderId", source = "order.orderId")
    PaymentDto toDto(Payment payment);
}
