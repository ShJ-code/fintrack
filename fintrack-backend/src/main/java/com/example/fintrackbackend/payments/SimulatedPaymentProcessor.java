package com.example.fintrackbackend.payments;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SimulatedPaymentProcessor implements PaymentProcessor {

    /** Pretend memory of past attempts so the same idempotency key returns the same answer. */
    private final ConcurrentHashMap<String, ChargeResult> ledger = new ConcurrentHashMap<>();

    @Override
    public ChargeResult charge(BigDecimal amount, String method, String idempotencyKey) {
        ChargeResult cached = ledger.get(idempotencyKey);
        if (cached != null) return cached;

        // Amounts ending in .13 always decline. This rule is for testing.
        boolean declined = amount.remainder(BigDecimal.ONE).compareTo(new BigDecimal("0.13")) == 0;

        ChargeResult result = declined
                ? new ChargeResult(null, false, "Card declined (simulated)")
                : new ChargeResult("sim_" + UUID.randomUUID(), true, null);

        ledger.put(idempotencyKey, result);
        return result;
    }
}
