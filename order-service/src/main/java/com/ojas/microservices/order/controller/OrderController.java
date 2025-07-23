package com.ojas.microservices.order.controller;

import com.ojas.microservices.order.client.InventoryClient;
import com.ojas.microservices.order.dto.InventoryRequest;
import com.ojas.microservices.order.dto.OrderRequest;
import com.ojas.microservices.order.dto.OrderResponse;
import com.ojas.microservices.order.exception.OrderNotFoundException;
import com.ojas.microservices.order.exception.ProductNotInStockException;
import com.ojas.microservices.order.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final InventoryClient inventoryClient;

    @PostMapping
    public ResponseEntity<OrderResponse> placeOrder(@RequestBody @Valid OrderRequest orderRequest) {
        try {
            OrderResponse response = orderService.placeOrder(orderRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (ProductNotInStockException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }


    @GetMapping
    public ResponseEntity<List<OrderResponse>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getOrderById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(orderService.getOrderById(id));
        } catch (OrderNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/reserve")
    public ResponseEntity<Boolean> reserveStock(@RequestBody InventoryRequest request) {
        boolean success = inventoryClient.reserveStock(request);
        return ResponseEntity.ok(success);
    }
}
