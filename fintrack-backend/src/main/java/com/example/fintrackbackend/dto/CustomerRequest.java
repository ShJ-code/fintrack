package com.example.fintrackbackend.dto;

public class CustomerRequest {
    private String companyName;
    private String email;
    public CustomerRequest() {}

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
