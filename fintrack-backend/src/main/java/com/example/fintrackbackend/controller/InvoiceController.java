package com.example.fintrackbackend.controller;

import com.example.fintrackbackend.model.Invoice;
import com.example.fintrackbackend.security.CurrentUserId;
import com.example.fintrackbackend.service.InvoiceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/invoices")
@CrossOrigin("http://localhost:5173")
public class InvoiceController {

    private final InvoiceService invoiceService;

    public InvoiceController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @GetMapping
    public List<Invoice> list(@CurrentUserId Integer userId) {
        return invoiceService.getInvoicesForUser(userId);
    }

    @PostMapping
    public ResponseEntity<Invoice> createInvoice(
        @CurrentUserId Integer userId,
        @RequestBody Invoice invoice) {
        return ResponseEntity.status(201).body(invoiceService.createInvoice(userId, invoice));
    }

    @PutMapping("/{invoiceId}")
    public Invoice update(@CurrentUserId Integer userId,
                          @PathVariable int invoiceId,
                          @RequestBody Invoice invoice) {
        return invoiceService.updateInvoice(invoiceId, userId, invoice);
    }

    @DeleteMapping("/{invoiceId}")
    public ResponseEntity<Void> delete(@CurrentUserId Integer userId,
                                       @PathVariable int invoiceId) {
        invoiceService.deleteInvoice(invoiceId, userId);
        return ResponseEntity.noContent().build();
    }
}
