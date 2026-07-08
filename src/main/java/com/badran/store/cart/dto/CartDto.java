package com.badran.store.cart.dto;

import lombok.Data;

import java.util.List;

@Data
public class CartDto {
    private Long cartId;
    private Long userId;
    private String sessionId;
    private List<CartItemDto> items;
}
