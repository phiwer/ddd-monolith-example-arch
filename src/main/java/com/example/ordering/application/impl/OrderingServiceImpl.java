package com.example.ordering.application.impl;

import com.example.catalog.application.data.ProductView;
import com.example.catalog.application.CatalogService;
import com.example.ordering.application.data.OrderLineView;
import com.example.ordering.application.data.OrderView;
import com.example.ordering.application.data.PlaceOrderCommand;
import com.example.ordering.application.OrderingService;
import com.example.ordering.domain.order.Order;
import com.example.ordering.domain.order.OrderRepository;
import com.example.shared.Money;
import com.example.shared.ProductId;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Currency;
import java.util.Optional;

/**
 * Implementation is package-private — other contexts can't see it.
 * They only see the OrderingService interface.
 *
 * Note the cross-context dependency: we inject CatalogService (an interface)
 * to look up product prices. We never touch Catalog's domain layer.
 */
@Service
@Transactional
class OrderingServiceImpl implements OrderingService {

    private final OrderRepository orderRepository;
    private final CatalogService catalogService; // ← cross-context call via interface

    OrderingServiceImpl(OrderRepository orderRepository, CatalogService catalogService) {
        this.orderRepository = orderRepository;
        this.catalogService = catalogService;
    }

    @Override
    public OrderView placeOrder(PlaceOrderCommand command) {
        var order = new Order();

        for (var line : command.lines()) {
            // Cross-context call: ask Catalog for the price via its public interface.
            // We get back a DTO, translate it into our domain's language, and move on.
            ProductView product = catalogService.findProduct(line.productId())
                    .orElseThrow(() -> new IllegalArgumentException("Unknown product: " + line.productId()));

            var unitPrice = new Money(product.price(), Currency.getInstance(product.currencyCode()));
            order.addLine(new ProductId(line.productId()), line.quantity(), unitPrice);
        }

        orderRepository.save(order);
        return toView(order);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<OrderView> findOrder(String orderId) {
        return orderRepository.findById(orderId)
                .map(this::toView);
    }

    @Override
    public void confirmOrder(String orderId) {
        var order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found: " + orderId));

        order.confirm();
        orderRepository.save(order);
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
