package com.example.ordering.domain.order;

import com.example.ordering.domain.payment.PaymentGateway;

import org.springframework.stereotype.Component;

/**
 * Domain service that coordinates an Order's confirmation with the
 * consumer-owned PaymentGateway port.
 *
 * Lives in ordering.domain — the coordination logic ("confirming an order
 * charges payment") IS domain knowledge, and this is the place to put it
 * when you want Order itself to stay pure (no injected dependencies).
 */
@Component
public class OrderConfirmation {

    private final PaymentGateway paymentGateway;

    public OrderConfirmation(PaymentGateway paymentGateway) {
        this.paymentGateway = paymentGateway;
    }

    public void confirm(Order order) {
        order.confirm();
        paymentGateway.charge(order.orderId(), order.total());
    }
}
