package com.ojas.microservices.order.service;

import com.ojas.microservices.order.client.InventoryClient;
import com.ojas.microservices.order.dto.InventoryRequest;
import com.ojas.microservices.order.dto.InventoryResponse;
import com.ojas.microservices.order.dto.OrderRequest;
import com.ojas.microservices.order.dto.OrderResponse;
import com.ojas.microservices.order.event.OrderPlacedEvent;
import com.ojas.microservices.order.exception.OrderNotFoundException;
import com.ojas.microservices.order.exception.ProductNotInStockException;
import com.ojas.microservices.order.model.Order;
import com.ojas.microservices.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final InventoryClient inventoryClient;
    private final KafkaTemplate<String, OrderPlacedEvent> kafkaTemplate;

    public OrderResponse placeOrder(OrderRequest orderRequest) {
        InventoryResponse response = inventoryClient.isInStock(orderRequest.skuCode(), orderRequest.quantity());

        if (!response.inStock()) throw new ProductNotInStockException("Product is not in stock");

        boolean reserved = inventoryClient.reserveStock(new InventoryRequest(
                orderRequest.skuCode(), orderRequest.quantity()));

        if (!reserved) throw new ProductNotInStockException("Unable to reserve stock");

        Order order = mapToOrder(orderRequest);
        orderRepository.save(order);

        var orderPlacedEvent = new OrderPlacedEvent(order.getOrderNumber(),
                orderRequest.userDetails().email(),
                orderRequest.userDetails().firstName(),
                orderRequest.userDetails().lastName());
        kafkaTemplate.send("order-placed", orderPlacedEvent);
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