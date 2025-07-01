package com.ojas.microservices.order.dto;

import com.ojas.microservices.order.model.Order;

import java.math.BigDecimal;

public record OrderResponse(Long id, String orderNumber, String skuCode, BigDecimal price, Integer quantity) {
    public static OrderResponse fromOrder(Order order) {
        return new OrderResponse(
                order.getId(),
                order.getOrderNumber(),
                order.getSkuCode(),
                order.getPrice(),
                order.getQuantity()
        );
    }
}

