package com.example.ordering.domain.order;

import com.example.shared.Money;
import com.example.shared.ProductId;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * Order aggregate in the Ordering context.
 *
 * Notice: there is no "Product" here. The Ordering context doesn't know
 * what a product looks like — it only holds a ProductId reference and
 * its own OrderLine representation with the data *it* cares about.
 */
public class Order {

    private final String orderId;
    private final List<OrderLine> lines;
    private OrderStatus status;

    public Order() {
        this.orderId = UUID.randomUUID().toString();
        this.lines = new ArrayList<>();
        this.status = OrderStatus.DRAFT;
    }

    public void addLine(ProductId productId, int quantity, Money unitPrice) {
        if (status != OrderStatus.DRAFT) {
            throw new IllegalStateException("Cannot modify a confirmed order");
        }
        lines.add(new OrderLine(productId, quantity, unitPrice));
    }

    public void confirm() {
        if (lines.isEmpty()) {
            throw new IllegalStateException("Cannot confirm an empty order");
        }
        this.status = OrderStatus.CONFIRMED;
    }

    public String orderId() { return orderId; }
    public OrderStatus status() { return status; }
    public List<OrderLine> lines() { return Collections.unmodifiableList(lines); }
}
