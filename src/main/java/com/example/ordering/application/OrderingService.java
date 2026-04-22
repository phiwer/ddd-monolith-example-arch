package com.example.ordering.application;

import com.example.ordering.application.data.OrderView;
import com.example.ordering.application.data.PlaceOrderCommand;

import java.util.Optional;

/**
 * Public interface of the Ordering bounded context.
 *
 * Other contexts depend on this + the DTOs. That's it.
 *
 * Three confirm* variants are kept side-by-side as a teaching artifact.
 * They all do the same thing (mark CONFIRMED + charge payment); they
 * differ in WHERE the coordination logic lives. See OrderingServiceImpl
 * for the discussion.
 */
public interface OrderingService {

    OrderView placeOrder(PlaceOrderCommand command);

    Optional<OrderView> findOrder(String orderId);

    /** Uses a domain service ({@code OrderConfirmation}) to coordinate. */
    void confirmOrder(String orderId);

    /** The application service itself orchestrates order + port in sequence. */
    void confirmOrderInApp(String orderId);

    /** The aggregate calls the port: {@code order.confirm(paymentGateway)}. */
    void confirmOrderInAggregate(String orderId);
}
