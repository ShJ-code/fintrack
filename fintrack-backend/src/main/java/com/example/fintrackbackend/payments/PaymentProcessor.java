package com.example.fintrackbackend.payments;

import java.math.BigDecimal;

public interface PaymentProcessor {

    record ChargeResult(String externalRef, boolean succeeded, String failureReason) {}

    /**
     * Attempt to charge or collect a given amount. The idempotencyKey ensures that
     * retries don't double-charge: calling twice with the same key returns the
     * outcome of the original attempt.
     */
    ChargeResult charge(BigDecimal amount, String method, String idempotencyKey);
}
