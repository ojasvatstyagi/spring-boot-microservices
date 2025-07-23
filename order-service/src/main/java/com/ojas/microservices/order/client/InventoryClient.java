package com.ojas.microservices.order.client;

import com.ojas.microservices.order.dto.InventoryRequest;
import com.ojas.microservices.order.dto.InventoryResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.PostExchange;

public interface InventoryClient {

    @GetExchange("/api/inventory")
    @CircuitBreaker(name = "inventory", fallbackMethod = "fallbackMethod")
    @Retry(name = "inventory")
    InventoryResponse isInStock(@RequestParam String skuCode, @RequestParam Integer quantity);

    default InventoryResponse fallbackMethod(String skuCode, Integer quantity, Throwable t) {
        return new InventoryResponse(skuCode, false);
    }
    @PostExchange("/api/inventory/reserve")
    @CircuitBreaker(name = "inventory", fallbackMethod = "reserveFallback")
    @Retry(name = "inventory")
    boolean reserveStock(@RequestBody InventoryRequest request);

    default boolean reserveFallback(InventoryRequest request, Throwable t) {
        return false;
    }

}