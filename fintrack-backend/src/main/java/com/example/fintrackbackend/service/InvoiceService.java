package com.example.fintrackbackend.service;

import com.example.fintrackbackend.model.Invoice;
import com.example.fintrackbackend.repository.CustomerRepository;
import com.example.fintrackbackend.repository.InvoiceRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final CustomerRepository customerRepository;

    public InvoiceService(InvoiceRepository invoiceRepository, CustomerRepository customerRepository) {
        this.invoiceRepository = invoiceRepository;
        this.customerRepository = customerRepository;
    }

    public List<Invoice> getInvoicesForUser(int userId) {
        return invoiceRepository.findByUserId(userId);
    }

    public Invoice createInvoice(int userId, Invoice invoice) {
        if (invoice.getAmount() == null
            || invoice.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Invoice amount must be positive");
        }
        if (invoice.getDueDate() == null) {
            throw new IllegalArgumentException("Invoice due date is required");
        }

        customerRepository.findByIdForUser(invoice.getCustomerId(), userId)
                .orElseThrow(() -> new IllegalArgumentException("Unknown customer"));

        if (invoice.getStatus() == null || invoice.getStatus().isBlank()) {
            invoice.setStatus("open");
        }

        int newId = invoiceRepository.insert(invoice);
        invoice.setInvoiceId(newId);
        return invoice;
    }

    public Invoice updateInvoice(int invoiceId, int userId, Invoice invoice) {
        if (invoice.getAmount() == null
                || invoice.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Invoice amount must be positive");
        }
        if (invoice.getDueDate() == null) {
            throw new IllegalArgumentException("Invoice due date is required");
        }

        int rows = invoiceRepository.update(invoiceId, userId, invoice.getAmount(), invoice.getDueDate(), invoice.getStatus());
        if (rows == 0) throw new NoSuchElementException("Invoice not found");

        invoice.setInvoiceId(invoiceId);
        return invoice;
    }

    public void deleteInvoice(int invoiceId, int userId) {
        int rows = invoiceRepository.delete(invoiceId, userId);
        if (rows == 0) throw new NoSuchElementException("Invoice not found");
    }
}
