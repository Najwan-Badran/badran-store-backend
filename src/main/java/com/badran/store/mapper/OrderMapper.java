package com.badran.store.mapper;

import com.badran.store.dto.model.OrderDto;
import com.badran.store.dto.model.OrderItemDto;
import com.badran.store.entity.Order;
import com.badran.store.entity.OrderItem;
import org.mapstruct.Mapper;

/**
 * MapStruct mapper for converting orders and order item lines into API DTOs.
 */
@Mapper(componentModel = "spring", uses = {CouponMapper.class})
public interface OrderMapper {
    /**
     * Converts an order aggregate into a response DTO.
     */
    OrderDto toDto(Order order);

    /**
     * Converts an order item entity into a response DTO.
     */
    OrderItemDto toDto(OrderItem item);
}
