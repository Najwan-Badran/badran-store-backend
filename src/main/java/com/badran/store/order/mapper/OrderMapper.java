package com.badran.store.order.mapper;

import com.badran.store.coupon.mapper.CouponMapper;
import com.badran.store.order.dto.OrderDto;
import com.badran.store.order.dto.OrderItemDto;
import com.badran.store.order.entity.Order;
import com.badran.store.order.entity.OrderItem;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {CouponMapper.class})
public interface OrderMapper {
    OrderDto toDto(Order order);
    OrderItemDto toDto(OrderItem item);
}
