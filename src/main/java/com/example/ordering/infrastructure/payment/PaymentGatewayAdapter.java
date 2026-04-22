package com.example.ordering.infrastructure.payment;

import com.example.ordering.domain.payment.PaymentGateway;
import com.example.ordering.domain.payment.PaymentReceipt;
import com.example.payment.application.PaymentService;
import com.example.payment.application.data.ChargeCommand;
import com.example.payment.application.data.ChargeResult;
import com.example.shared.Money;

import org.springframework.stereotype.Component;

/**
 * Adapter sitting between Ordering's port (PaymentGateway) and the Payment
 * bounded context's public API (PaymentService).
 *
 * This is the ONLY place in Ordering that is allowed to import payment.* —
 * the ArchUnit rule in BoundedContextBoundaryTest enforces that.
 */
@Component
class PaymentGatewayAdapter implements PaymentGateway {

    private final PaymentService paymentService;

    PaymentGatewayAdapter(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @Override
    public PaymentReceipt charge(String orderId, Money amount) {
        ChargeResult result = paymentService.authorize(new ChargeCommand(
                orderId,
                amount.amount(),
                amount.currency().getCurrencyCode()
        ));
        return new PaymentReceipt(result.paymentId(), result.authorizedAt());
    }
}
