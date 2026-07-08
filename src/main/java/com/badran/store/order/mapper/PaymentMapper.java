package com.badran.store.order.mapper;

import com.badran.store.order.dto.PaymentDto;
import com.badran.store.order.entity.Payment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PaymentMapper {
    @Mapping(target = "orderId", source = "order.orderId")
    PaymentDto toDto(Payment payment);
}
