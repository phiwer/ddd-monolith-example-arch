package com.example.catalog.application.data;

import java.math.BigDecimal;

public record CreateProductCommand(
        String productId,
        String name,
        String description,
        String category,
        BigDecimal price,
        String currencyCode
) {}
