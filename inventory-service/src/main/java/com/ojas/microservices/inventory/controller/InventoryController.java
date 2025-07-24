package com.ojas.microservices.inventory.controller;

import com.ojas.microservices.inventory.dto.InventoryRequest;
import com.ojas.microservices.inventory.dto.InventoryResponse;
import com.ojas.microservices.inventory.dto.InventoryUpdateRequest;
import com.ojas.microservices.inventory.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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

    @PostMapping
    public ResponseEntity<Void> createInventory(@RequestBody InventoryRequest request) {
        inventoryService.createInventory(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{skuCode}")
    public ResponseEntity<Void> updateInventory(@PathVariable String skuCode,
                                                @RequestBody InventoryUpdateRequest request) {
        inventoryService.updateInventory(skuCode, request.quantity());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/reserve")
    public ResponseEntity<Boolean> reserveStock(@RequestBody InventoryRequest request) {
        boolean success = inventoryService.reserveStock(request.skuCode(), request.quantity());
        return ResponseEntity.ok(success);
    }
}
