package com.example.fintrackbackend.model;

public class Vendor {
    private int vendorId;
    private int userId;
    private String companyName;

    public Vendor() {}

    public Vendor(int vendorId, int userId, String companyName) {
        this.vendorId = vendorId;
        this.userId = userId;
        this.companyName = companyName;
    }

    public int getVendorId() {
        return vendorId;
    }

    public void setVendorId(int vendorId) {
        this.vendorId = vendorId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}
