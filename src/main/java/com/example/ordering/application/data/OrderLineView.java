package com.example.ordering.application.data;

import java.math.BigDecimal;

public record OrderLineView(String productId, int quantity, BigDecimal unitPrice, String currencyCode) {}
