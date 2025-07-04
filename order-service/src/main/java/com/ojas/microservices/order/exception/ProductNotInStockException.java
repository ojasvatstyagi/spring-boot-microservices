package com.ojas.microservices.order.exception;

public class ProductNotInStockException extends RuntimeException {
    public ProductNotInStockException(String message) {
        super(message);
    }
}
