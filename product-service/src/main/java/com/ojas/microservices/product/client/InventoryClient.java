package com.ojas.microservices.product.client;

import com.ojas.microservices.product.dto.InventoryRequest;
import com.ojas.microservices.product.dto.InventoryUpdateRequest;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;
import org.springframework.web.service.annotation.PutExchange;

@HttpExchange("/api/inventory")
public interface InventoryClient {

    @PostExchange
    void createInventory(@RequestBody InventoryRequest request);

    @PutExchange("/{skuCode}")
    void updateInventory(@PathVariable String skuCode, @RequestBody InventoryUpdateRequest request);
}

