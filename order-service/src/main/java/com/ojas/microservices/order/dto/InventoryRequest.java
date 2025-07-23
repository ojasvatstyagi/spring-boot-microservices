package com.ojas.microservices.order.dto;

public record InventoryRequest(String skuCode, Integer quantity) {}