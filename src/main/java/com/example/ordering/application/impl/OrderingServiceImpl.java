package com.example.ordering.application.impl;

import com.example.ordering.application.data.OrderLineView;
import com.example.ordering.application.data.OrderView;
import com.example.ordering.application.data.PlaceOrderCommand;
import com.example.ordering.application.OrderingService;
import com.example.ordering.domain.order.Order;
import com.example.ordering.domain.order.OrderConfirmation;
import com.example.ordering.domain.order.OrderRepository;
import com.example.ordering.domain.payment.PaymentGateway;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Implementation is package-private — other contexts can't see it.
 * They only see the OrderingService interface.
 *
 * NOTE FOR MOB SESSION:
 * placeOrder is intentionally unfinished. Ordering needs a unit price for each
 * line, but AddLineCommand only carries productId + quantity. Prices are owned
 * by another bounded context. The session task: define a consumer-owned port
 * in ordering.domain.* (mirroring PaymentGateway), implement its adapter in
 * ordering.infrastructure.*, wire it here, and make OrderingServiceApplicationTest pass.
 * Use the Payment wiring as a template — and when you wire the Catalog call,
 * discuss which of the three confirmOrder* shapes below is the right home for it.
 *
 * --- Three shapes of the confirm-and-charge flow ---
 *
 *  confirmOrder            — domain service (OrderConfirmation) coordinates.
 *                            Order stays pure; coordination is domain knowledge
 *                            but kept out of the aggregate itself.
 *
 *  confirmOrderInApp       — application service orchestrates step-by-step.
 *                            Simplest to read; aggregate has no dependencies;
 *                            but "confirm must charge" is a convention, not an
 *                            invariant enforced by the domain.
 *
 *  confirmOrderInAggregate — aggregate calls the port: order.confirm(paymentGateway).
 *                            Rule is baked into the aggregate — impossible to
 *                            bypass. Cost: Order.confirm(...) now needs a stub
 *                            PaymentGateway in its unit tests.
 */
@Service
@Transactional
class OrderingServiceImpl implements OrderingService {

    private final OrderRepository orderRepository;
    private final PaymentGateway paymentGateway;
    private final OrderConfirmation orderConfirmation;

    OrderingServiceImpl(OrderRepository orderRepository,
                        PaymentGateway paymentGateway,
                        OrderConfirmation orderConfirmation) {
        this.orderRepository = orderRepository;
        this.paymentGateway = paymentGateway;
        this.orderConfirmation = orderConfirmation;
    }

    @Override
    public OrderView placeOrder(PlaceOrderCommand command) {
        throw new UnsupportedOperationException(
                "placeOrder not yet wired up. Mob session: fetch the unit price " +
                "for each line from the Catalog bounded context via a consumer-owned port.");
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<OrderView> findOrder(String orderId) {
        return orderRepository.findById(orderId)
                .map(this::toView);
    }

    @Override
    public void confirmOrder(String orderId) {
        var order = loadOrder(orderId);
        orderConfirmation.confirm(order);
        orderRepository.save(order);
    }

    @Override
    public void confirmOrderInApp(String orderId) {
        var order = loadOrder(orderId);
        order.confirm();
        paymentGateway.charge(order.orderId(), order.total());
        orderRepository.save(order);
    }

    @Override
    public void confirmOrderInAggregate(String orderId) {
        var order = loadOrder(orderId);
        order.confirm(paymentGateway);
        orderRepository.save(order);
    }

    private Order loadOrder(String orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found: " + orderId));
    }

    private OrderView toView(Order order) {
        var lines = order.lines().stream()
                .map(l -> new OrderLineView(
                        l.productId().value(),
                        l.quantity(),
                        l.unitPrice().amount(),
                        l.unitPrice().currency().getCurrencyCode()))
                .toList();

        return new OrderView(order.orderId(), order.status().name(), lines);
    }
}
