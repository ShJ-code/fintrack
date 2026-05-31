package com.example.fintrackbackend.model;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

public class Invoice {
    private int invoiceId;
    private int customerId;
    private String customerName;
    private String customerEmail;
    private BigDecimal amount;
    private Date dueDate;
    private String status;
    private Timestamp createdAt;

    public Invoice() {}

    public Invoice(int invoiceId, int customerId, BigDecimal amount, Date dueDate, String status, Timestamp createdAt) {
        this.invoiceId = invoiceId;
        this.customerId = customerId;
        this.amount = amount;
        this.dueDate = dueDate;
        this.status = status;
        this.createdAt = createdAt;
    }

    public Invoice(int invoiceId, int customerId, BigDecimal amount, Date dueDate, String status, Timestamp createdAt, String customerName, String customerEmail) {
        this.invoiceId = invoiceId;
        this.customerId = customerId;
        this.amount = amount;
        this.dueDate = dueDate;
        this.status = status;
        this.createdAt = createdAt;
        this.customerName = customerName;
        this.customerEmail = customerEmail;
    }

    public int getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(int invoiceId) {
        this.invoiceId = invoiceId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }
}
