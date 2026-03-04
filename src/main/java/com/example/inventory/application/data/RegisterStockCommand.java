package com.example.inventory.application.data;

public record RegisterStockCommand(String productId, int initialQuantity, int reorderThreshold) {}
