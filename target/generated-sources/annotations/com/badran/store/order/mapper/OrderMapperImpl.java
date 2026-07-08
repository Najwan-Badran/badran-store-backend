package com.badran.store.order.mapper;

import com.badran.store.coupon.mapper.CouponMapper;
import com.badran.store.order.dto.OrderDto;
import com.badran.store.order.dto.OrderItemDto;
import com.badran.store.order.entity.Order;
import com.badran.store.order.entity.OrderItem;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-07-08T15:46:00+0300",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.10 (Homebrew)"
)
@Component
public class OrderMapperImpl implements OrderMapper {

    @Autowired
    private CouponMapper couponMapper;

    @Override
    public OrderDto toDto(Order order) {
        if ( order == null ) {
            return null;
        }

        OrderDto orderDto = new OrderDto();

        orderDto.setOrderId( order.getOrderId() );
        orderDto.setPublicId( order.getPublicId() );
        orderDto.setOrderNumber( order.getOrderNumber() );
        orderDto.setUserId( order.getUserId() );
        orderDto.setGuestName( order.getGuestName() );
        orderDto.setGuestPhone( order.getGuestPhone() );
        orderDto.setGuestEmail( order.getGuestEmail() );
        orderDto.setFulfillmentMethod( order.getFulfillmentMethod() );
        orderDto.setDeliveryAddressId( order.getDeliveryAddressId() );
        orderDto.setDeliveryCity( order.getDeliveryCity() );
        orderDto.setDeliveryZone( order.getDeliveryZone() );
        orderDto.setDeliveryAddressLine( order.getDeliveryAddressLine() );
        orderDto.setDeliveryFee( order.getDeliveryFee() );
        orderDto.setStatus( order.getStatus() );
        orderDto.setSubtotal( order.getSubtotal() );
        orderDto.setCoupon( couponMapper.toDto( order.getCoupon() ) );
        orderDto.setDiscountAmount( order.getDiscountAmount() );
        orderDto.setTotal( order.getTotal() );
        orderDto.setCreatedAt( order.getCreatedAt() );
        orderDto.setItems( orderItemListToOrderItemDtoList( order.getItems() ) );

        return orderDto;
    }

    @Override
    public OrderItemDto toDto(OrderItem item) {
        if ( item == null ) {
            return null;
        }

        OrderItemDto orderItemDto = new OrderItemDto();

        orderItemDto.setOrderItemId( item.getOrderItemId() );
        orderItemDto.setProductId( item.getProductId() );
        orderItemDto.setQuantity( item.getQuantity() );
        orderItemDto.setUnitPrice( item.getUnitPrice() );
        orderItemDto.setLineTotal( item.getLineTotal() );

        return orderItemDto;
    }

    protected List<OrderItemDto> orderItemListToOrderItemDtoList(List<OrderItem> list) {
        if ( list == null ) {
            return null;
        }

        List<OrderItemDto> list1 = new ArrayList<OrderItemDto>( list.size() );
        for ( OrderItem orderItem : list ) {
            list1.add( toDto( orderItem ) );
        }

        return list1;
    }
}
