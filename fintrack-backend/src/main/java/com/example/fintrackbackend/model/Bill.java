package com.example.fintrackbackend.model;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

public class Bill {
    private int billId;
    private int vendorId;
    private String vendorName;
    private BigDecimal amount;
    private Date dueDate;
    private String status;
    private Timestamp createdAt;

    public Bill() {}

    public Bill(int billId, int vendorId, BigDecimal amount, Date dueDate, String status, Timestamp createdAt) {
        this.billId = billId;
        this.vendorId = vendorId;
        this.amount = amount;
        this.dueDate = dueDate;
        this.status = status;
        this.createdAt = createdAt;
    }

    public Bill(int billId, int vendorId, String vendorName, BigDecimal amount, Date dueDate, String status, Timestamp createdAt) {
        this.billId = billId;
        this.vendorId = vendorId;
        this.vendorName = vendorName;
        this.amount = amount;
        this.dueDate = dueDate;
        this.status = status;
        this.createdAt = createdAt;
    }

    public int getBillId() {
        return billId;
    }

    public void setBillId(int billId) {
        this.billId = billId;
    }

    public int getVendorId() {
        return vendorId;
    }

    public void setVendorId(int vendorId) {
        this.vendorId = vendorId;
    }

    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
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

    @Override
    public String toString() {
        return "Bill{" +
                "billId=" + billId +
                ", vendorId=" + vendorId +
                ", vendorName='" + vendorName + '\'' +
                ", amount=" + amount +
                ", dueDate=" + dueDate +
                ", status='" + status + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
