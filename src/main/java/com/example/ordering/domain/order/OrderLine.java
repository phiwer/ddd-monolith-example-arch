package com.example.ordering.domain.order;

import com.example.shared.Money;
import com.example.shared.ProductId;

import jakarta.persistence.Embeddable;

/**
 * What "product" looks like inside the Ordering context —
 * just a reference ID, a quantity, and a price snapshot.
 */
@Embeddable
public record OrderLine(ProductId productId, int quantity, Money unitPrice) {

    public OrderLine {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }
    }
}
