package com.example.payment.application.data;

import java.math.BigDecimal;

public record ChargeCommand(
        String referenceId,
        BigDecimal amount,
        String currencyCode
) {}
