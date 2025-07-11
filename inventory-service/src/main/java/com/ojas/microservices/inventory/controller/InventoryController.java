package com.ojas.microservices.inventory.controller;

import com.ojas.microservices.inventory.dto.InventoryResponse;
import com.ojas.microservices.inventory.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    @GetMapping
    public ResponseEntity<InventoryResponse> isInStock(@RequestParam String skuCode,
                                    @RequestParam Integer quantity) {
        InventoryResponse inStock = inventoryService.isInStock(skuCode, quantity);
        return ResponseEntity.ok(inStock);
    }
}