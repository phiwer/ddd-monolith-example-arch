package com.example.ordering.domain.payment;

import java.time.Instant;

/**
 * What Ordering wants back from a charge, in its own terms.
 * Whatever richer structure Payment returns is stripped/translated by the adapter.
 */
public record PaymentReceipt(String transactionId, Instant chargedAt) {}
