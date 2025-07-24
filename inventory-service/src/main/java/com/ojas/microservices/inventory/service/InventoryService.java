package com.ojas.microservices.inventory.service;

import com.ojas.microservices.inventory.dto.InventoryRequest;
import com.ojas.microservices.inventory.dto.InventoryResponse;
import com.ojas.microservices.inventory.model.Inventory;
import com.ojas.microservices.inventory.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    @Transactional(readOnly = true)
    public InventoryResponse isInStock(String skuCode, Integer quantity) {
        boolean available = inventoryRepository.existsBySkuCodeAndQuantityIsGreaterThanEqual(skuCode, quantity);
        return new InventoryResponse(skuCode, available);
    }

    public boolean reserveStock(String skuCode, int quantity) {
        Inventory inventory = inventoryRepository.findBySkuCode(skuCode)
                .orElseThrow(() -> new RuntimeException("SKU not found"));

        if (inventory.getQuantity() >= quantity) {
            inventory.setQuantity(inventory.getQuantity() - quantity);
            inventoryRepository.save(inventory);
            return true;
        }
        return false;
    }

    @Transactional
    public void createInventory(InventoryRequest request) {
        Inventory inventory = new Inventory();
        inventory.setSkuCode(request.skuCode());
        inventory.setQuantity(request.quantity());
        inventoryRepository.save(inventory);
    }

    @Transactional
    public void updateInventory(String skuCode, int quantity) {
        Inventory inventory = inventoryRepository.findBySkuCode(skuCode)
                .orElseThrow(() -> new RuntimeException("SKU not found: " + skuCode));
        inventory.setQuantity(quantity);
        inventoryRepository.save(inventory);
    }
}