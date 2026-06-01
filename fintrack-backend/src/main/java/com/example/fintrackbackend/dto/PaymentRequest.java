package com.example.fintrackbackend.dto;

public class PaymentRequest {
    private Integer billId;
    private Integer invoiceId;
    private String method;
    private String idempotencyKey;

    public PaymentRequest() {}

    public PaymentRequest(Integer billId, Integer invoiceId, String method, String idempotencyKey) {
        this.billId = billId;
        this.invoiceId = invoiceId;
        this.method = method;
        this.idempotencyKey = idempotencyKey;
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

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getIdempotencyKey() {
        return idempotencyKey;
    }

    public void setIdempotencyKey(String idempotencyKey) {
        this.idempotencyKey = idempotencyKey;
    }
}
