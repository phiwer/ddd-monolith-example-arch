package com.example.payment.application.impl;

import com.example.payment.application.PaymentService;
import com.example.payment.application.data.ChargeCommand;
import com.example.payment.application.data.ChargeResult;

import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

/**
 * Stub implementation for the demo. Always authorizes, returns a fake payment id.
 * A real adapter would talk to Stripe / Adyen / an internal payment processor.
 */
@Service
class StubPaymentService implements PaymentService {

    @Override
    public ChargeResult authorize(ChargeCommand command) {
        return new ChargeResult(
                UUID.randomUUID().toString(),
                "AUTHORIZED",
                Instant.now()
        );
    }
}
