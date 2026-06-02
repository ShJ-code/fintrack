package com.example.fintrackbackend.controller;

import com.example.fintrackbackend.dto.PaymentRequest;
import com.example.fintrackbackend.model.Payment;
import com.example.fintrackbackend.repository.PaymentRepository;
import com.example.fintrackbackend.security.CurrentUserId;
import com.example.fintrackbackend.service.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    private final PaymentService paymentService;
    private final PaymentRepository paymentRepository;

    public PaymentController(PaymentService paymentService, PaymentRepository paymentRepository) {
        this.paymentService = paymentService;
        this.paymentRepository = paymentRepository;
    }

    @GetMapping
    public List<Payment> list(@CurrentUserId Integer userId) {
        return paymentRepository.findByUserId(userId);
    }

    @PostMapping
    public ResponseEntity<Payment> charge(@CurrentUserId Integer userId,
                                          @RequestBody PaymentRequest req) {
        if ((req.getBillId() == null) == (req.getInvoiceId() == null)) {
            throw new IllegalArgumentException("Provide exactly one of billId or invoiceId");
        }
        if (req.getIdempotencyKey() == null || req.getIdempotencyKey().isBlank()) {
            throw new IllegalArgumentException("idempotencyKey is required");
        }
        Payment p = req.getBillId() != null
                ? paymentService.payBill(userId, req.getBillId(), req.getMethod(), req.getIdempotencyKey())
                : paymentService.payInvoice(userId, req.getInvoiceId(), req.getMethod(), req.getIdempotencyKey());
        return ResponseEntity.ok(p);
    }
}
