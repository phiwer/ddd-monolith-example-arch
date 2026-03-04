package com.example.catalog.application.data;

import java.math.BigDecimal;

public record ProductView(
        String productId,
        String name,
        String description,
        String category,
        BigDecimal price,
        String currencyCode
) {}
