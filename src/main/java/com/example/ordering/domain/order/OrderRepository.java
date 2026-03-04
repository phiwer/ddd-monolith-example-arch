package com.example.ordering.domain.order;

import java.util.Optional;

public interface OrderRepository {

    Optional<Order> findById(String orderId);

    void save(Order order);
}
