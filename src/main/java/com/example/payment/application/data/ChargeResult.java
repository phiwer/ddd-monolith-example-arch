package com.example.payment.application.data;

import java.time.Instant;

public record ChargeResult(
        String paymentId,
        String status,
        Instant authorizedAt
) {}
