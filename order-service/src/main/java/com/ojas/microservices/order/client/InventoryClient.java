package com.ojas.microservices.order.client;

import com.ojas.microservices.order.dto.InventoryResponse;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;

public interface InventoryClient {

    @GetExchange("/api/inventory")
    InventoryResponse isInStock(@RequestParam String skuCode, @RequestParam Integer quantity);

}

