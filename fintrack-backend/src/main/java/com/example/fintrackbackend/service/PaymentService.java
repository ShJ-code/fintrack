package com.example.fintrackbackend.service;

import com.example.fintrackbackend.model.Bill;
import com.example.fintrackbackend.model.Invoice;
import com.example.fintrackbackend.model.Payment;
import com.example.fintrackbackend.payments.PaymentProcessor;
import com.example.fintrackbackend.repository.BillRepository;
import com.example.fintrackbackend.repository.InvoiceRepository;
import com.example.fintrackbackend.repository.PaymentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final BillRepository billRepository;
    private final InvoiceRepository invoiceRepository;
    private final PaymentProcessor paymentProcessor;

    public PaymentService(PaymentRepository paymentRepository,
                          BillRepository billRepository,
                          InvoiceRepository invoiceRepository,
                          PaymentProcessor paymentProcessor) {
        this.paymentRepository = paymentRepository;
        this.billRepository = billRepository;
        this.invoiceRepository = invoiceRepository;
        this.paymentProcessor = paymentProcessor;
    }

    @Transactional
    public Payment payBill(int userId, int billId, String method, String idempotencyKey) {
        // 1. Idempotency: if a payment with this key already exists, return it as-is.
        var existing = paymentRepository.findByIdempotencyKey((idempotencyKey));
        if (existing.isPresent()) return existing.get();

        // 2. Ownership + load.
        Bill bill = billRepository.findByIdForUser(billId, userId)
                .orElseThrow(() -> new NoSuchElementException("Bill not found"));
        if ("paid".equalsIgnoreCase(bill.getStatus())) {
            throw new IllegalArgumentException("Bill is already paid");
        }

        // 3. Create the pending payment row first, so even a crash leaves a trail.
        Payment p = new Payment();
        p.setBillId(billId);
        p.setAmount(bill.getAmount());
        p.setMethod(method);
        p.setIdempotencyKey(idempotencyKey);
        int paymentId = paymentRepository.insertPending(p);
        p.setPaymentId(paymentId);

        // 4. Call the processor.
        PaymentProcessor.ChargeResult result = paymentProcessor.charge(bill.getAmount(), method, idempotencyKey);

        // 5. Finalize.
        if (result.succeeded()) {
            paymentRepository.markSucceeded(paymentId, result.externalRef());
            billRepository.markPaid(billId, userId);
            p.setStatus("succeeded");
            p.setExternalRef(result.externalRef());
        } else {
            paymentRepository.markFailed(paymentId, result.failureReason());
            p.setStatus("failed");
            p.setFailureReason(result.failureReason());
        }
        return p;
    }

    @Transactional
    public Payment payInvoice(int userId, int invoiceId, String method, String idempotencyKey) {
        var existing = paymentRepository.findByIdempotencyKey(idempotencyKey);
        if (existing.isPresent()) return existing.get();

        Invoice inv = invoiceRepository.findByIdForUser(invoiceId, userId)
                .orElseThrow(() -> new NoSuchElementException("Invoice not found"));
        if ("paid".equalsIgnoreCase(inv.getStatus())) {
            throw new IllegalArgumentException("Invoice is already paid");
        }

        Payment p = new Payment();
        p.setInvoiceId(invoiceId);
        p.setAmount(inv.getAmount());
        p.setMethod(method);
        p.setIdempotencyKey(idempotencyKey);
        int paymentId = paymentRepository.insertPending(p);
        p.setPaymentId(paymentId);

        var result = paymentProcessor.charge(inv.getAmount(), method, idempotencyKey);

        if (result.succeeded()) {
            paymentRepository.markSucceeded(paymentId, result.externalRef());
            invoiceRepository.markPaid(invoiceId, userId);
            p.setStatus("succeeded");
            p.setExternalRef(result.externalRef());
        } else {
            paymentRepository.markFailed(paymentId, result.failureReason());
            p.setStatus("failed");
            p.setFailureReason(result.failureReason());
        }
        return p;
    }
}
