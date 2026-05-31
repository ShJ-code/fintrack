package com.example.fintrackbackend.model;

public class Customer {
    private int customerId;
    private int userId;
    private String companyName;
    private String email;

    public Customer() {}

    public Customer(int customerId, int userId, String companyName, String email) {
        this.customerId = customerId;
        this.userId = userId;
        this.companyName = companyName;
        this.email = email;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
