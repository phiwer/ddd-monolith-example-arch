package com.example.inventory.application.data;

public record StockView(String productId, int quantityOnHand, boolean needsReorder) {}
