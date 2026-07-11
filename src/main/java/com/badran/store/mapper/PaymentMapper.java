package com.badran.store.mapper;

import com.badran.store.dto.model.PaymentDto;
import com.badran.store.entity.Payment;
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
