package com.example.payment.application;

import com.example.payment.application.data.ChargeCommand;
import com.example.payment.application.data.ChargeResult;

/**
 * The public interface of the Payment bounded context.
 *
 * Other contexts never import this directly from their domain. They define
 * their own port (e.g. ordering.domain.payment.PaymentGateway) and an
 * infrastructure adapter that translates to this interface.
 */
public interface PaymentService {

    ChargeResult authorize(ChargeCommand command);
}
