package com.example.ordering.infrastructure;

import com.example.ordering.domain.order.Order;
import com.example.ordering.domain.order.OrderRepository;

import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
class InMemoryOrderRepository implements OrderRepository {

    private final Map<String, Order> store = new ConcurrentHashMap<>();

    @Override
    public Optional<Order> findById(String orderId) {
        return Optional.ofNullable(store.get(orderId));
    }

    @Override
    public void save(Order order) {
        store.put(order.orderId(), order);
    }
}
