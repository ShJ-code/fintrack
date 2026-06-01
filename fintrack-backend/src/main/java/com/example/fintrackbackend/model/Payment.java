package com.example.fintrackbackend.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Payment {
    private int paymentId;
    private Integer billId;
    private Integer invoiceId;
    private BigDecimal amount;
    private String method;
    private String status;
    private String externalRef;
    private String failureReason;
    private String idempotencyKey;
    private Timestamp createdAt;

    public Payment() {}

    public Payment(int paymentId, Integer billId, Integer invoiceId, BigDecimal amount, String method, String status, String externalRef, String failureReason, String idempotencyKey, Timestamp createdAt) {
        this.paymentId = paymentId;
        this.billId = billId;
        this.invoiceId = invoiceId;
        this.amount = amount;
        this.method = method;
        this.status = status;
        this.externalRef = externalRef;
        this.failureReason = failureReason;
        this.idempotencyKey = idempotencyKey;
        this.createdAt = createdAt;
    }

    public int getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(int paymentId) {
        this.paymentId = paymentId;
    }

    public Integer getBillId() {
        return billId;
    }

    public void setBillId(Integer billId) {
        this.billId = billId;
    }

    public Integer getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(Integer invoiceId) {
        this.invoiceId = invoiceId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getExternalRef() {
        return externalRef;
    }

    public void setExternalRef(String externalRef) {
        this.externalRef = externalRef;
    }

    public String getFailureReason() {
        return failureReason;
    }

    public void setFailureReason(String failureReason) {
        this.failureReason = failureReason;
    }

    public String getIdempotencyKey() {
        return idempotencyKey;
    }

    public void setIdempotencyKey(String idempotencyKey) {
        this.idempotencyKey = idempotencyKey;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}
