package com.ojas.microservices.product.service;

import com.ojas.microservices.product.client.InventoryClient;
import com.ojas.microservices.product.dto.InventoryRequest;
import com.ojas.microservices.product.dto.InventoryUpdateRequest;
import com.ojas.microservices.product.dto.ProductRequest;
import com.ojas.microservices.product.dto.ProductResponse;
import com.ojas.microservices.product.exception.ProductNotFoundException;
import com.ojas.microservices.product.model.Product;
import com.ojas.microservices.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final InventoryClient inventoryClient;
    private static final String PRODUCT_NOT_FOUND = "Product not found with id: ";

    public ProductResponse createProduct(ProductRequest productRequest) {
        Product product = Product.builder()
                .name(productRequest.name())
                .description(productRequest.description())
                .price(productRequest.price())
                .skuCode(productRequest.skuCode())
                .build();

        productRepository.save(product);

        // Add product to inventory with initial quantity (say 0 or configurable)
        inventoryClient.createInventory(new InventoryRequest(product.getSkuCode(), productRequest.quantity()));

        return mapToProductResponse(product);
    }

    public List<ProductResponse> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return products.stream().map(this::mapToProductResponse).toList();
    }

    private ProductResponse mapToProductResponse(Product product) {
        return new ProductResponse(product.getId(), product.getName(),
                product.getDescription(), product.getPrice(), product.getSkuCode());
    }

    public ProductResponse getProductById(String id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(PRODUCT_NOT_FOUND + id));
        return mapToProductResponse(product);
    }

    public ProductResponse updateProduct(String id, ProductRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(PRODUCT_NOT_FOUND + id));

        product.setName(request.name());
        product.setDescription(request.description());
        product.setPrice(request.price());
        product.setSkuCode(request.skuCode());

        productRepository.save(product);

        // Optionally update inventory if SKU or quantity changes
        inventoryClient.updateInventory(product.getSkuCode(), new InventoryUpdateRequest(request.quantity())); // or updated quantity

        return mapToProductResponse(product);
    }

    public void deleteProduct(String id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(PRODUCT_NOT_FOUND + id));
        productRepository.delete(product);
    }
}
