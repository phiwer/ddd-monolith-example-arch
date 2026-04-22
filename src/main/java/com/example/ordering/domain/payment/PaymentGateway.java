package com.example.ordering.domain.payment;

import com.example.shared.Money;

/**
 * Consumer-owned port. The Ordering context declares WHAT it needs
 * ("charge this amount against this order") in its own vocabulary.
 *
 * Ordering knows nothing about ChargeCommand, ChargeResult, or any other
 * type owned by the Payment bounded context. The translation happens in
 * ordering.infrastructure.payment.PaymentGatewayAdapter.
 */
public interface PaymentGateway {

    PaymentReceipt charge(String orderId, Money amount);
}
