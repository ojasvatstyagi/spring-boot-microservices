package com.ojas.microservices.order.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record OrderRequest(
        @NotBlank String skuCode,
        @NotNull @DecimalMin("0.1") BigDecimal price,
        @NotNull @Min(1) Integer quantity
) {}