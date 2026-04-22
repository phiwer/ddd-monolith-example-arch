package com.example.ordering.domain.order;

import com.example.ordering.domain.payment.PaymentGateway;
import com.example.shared.Money;
import com.example.shared.ProductId;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;

import java.math.BigDecimal;
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
@Entity
@Table(name = "orders")
public class Order {

    @Id
    private String orderId;

    @ElementCollection
    @CollectionTable(name = "order_lines", joinColumns = @JoinColumn(name = "order_id"))
    private List<OrderLine> lines;

    @Enumerated(EnumType.STRING)
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

    /**
     * Alternative to {@link #confirm()}: the aggregate itself invokes the
     * consumer-owned port. Pattern trade-off: the business rule "confirming
     * an order charges payment" is impossible to bypass because it lives
     * inside the aggregate, but Order's unit tests now need a PaymentGateway
     * stub.
     */
    public void confirm(PaymentGateway paymentGateway) {
        confirm();
        paymentGateway.charge(orderId, total());
    }

    public Money total() {
        if (lines.isEmpty()) {
            throw new IllegalStateException("Cannot total an empty order");
        }
        BigDecimal sum = lines.stream()
                .map(l -> l.unitPrice().amount().multiply(BigDecimal.valueOf(l.quantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return new Money(sum, lines.get(0).unitPrice().currency());
    }

    public String orderId() { return orderId; }
    public OrderStatus status() { return status; }
    public List<OrderLine> lines() { return Collections.unmodifiableList(lines); }
}
