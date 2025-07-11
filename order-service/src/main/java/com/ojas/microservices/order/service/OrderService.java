package com.ojas.microservices.order.service;

import com.ojas.microservices.order.dto.OrderRequest;
import com.ojas.microservices.order.dto.OrderResponse;
import com.ojas.microservices.order.exception.OrderNotFoundException;
import com.ojas.microservices.order.model.Order;
import com.ojas.microservices.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderResponse placeOrder(OrderRequest orderRequest) {
        var order = mapToOrder(orderRequest);
        orderRepository.save(order);
        return OrderResponse.fromOrder(order);
    }


    private static Order mapToOrder(OrderRequest orderRequest) {
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());
        order.setPrice(orderRequest.price());
        order.setQuantity(orderRequest.quantity());
        order.setSkuCode(orderRequest.skuCode());
        return order;
    }

    public List<OrderResponse> getAllOrders() {
        return orderRepository.findAll()
                .stream()
                .map(OrderResponse::fromOrder)
                .toList();
    }

    public OrderResponse getOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with id: " + id));
        return OrderResponse.fromOrder(order);
    }
}