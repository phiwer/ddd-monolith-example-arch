package com.example.ordering.application;

import com.example.ordering.application.data.OrderView;
import com.example.ordering.application.data.PlaceOrderCommand;

import java.util.Optional;

/**
 * Public interface of the Ordering bounded context.
 *
 * Other contexts depend on this + the DTOs. That's it.
 */
public interface OrderingService {

    OrderView placeOrder(PlaceOrderCommand command);

    Optional<OrderView> findOrder(String orderId);

    void confirmOrder(String orderId);
}
