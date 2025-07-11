package com.ojas.microservices.order.client;

import com.ojas.microservices.order.dto.InventoryResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "inventory", url = "${inventory.url}")
public interface InventoryClient {

    @GetMapping("/api/inventory")
    InventoryResponse isInStock(@RequestParam String skuCode, @RequestParam Integer quantity);

}

