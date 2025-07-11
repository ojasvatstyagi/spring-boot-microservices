package com.ojas.microservices.inventory.dto;

public record InventoryResponse(String skuCode, boolean inStock) { }