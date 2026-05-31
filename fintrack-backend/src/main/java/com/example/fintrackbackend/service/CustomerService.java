package com.example.fintrackbackend.service;

import com.example.fintrackbackend.model.Customer;
import com.example.fintrackbackend.repository.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public List<Customer> getCustomers(Integer userId) {
        return customerRepository.findByUserId(userId);
    }

    public Customer create(int userId, String companyName, String email) {
        validate(companyName, email);
        int newId = customerRepository.insert(userId, companyName.trim(), email.trim().toLowerCase());
        return new Customer(newId, userId, companyName.trim(), email.trim().toLowerCase());
    }

    public Customer update(int customerId, int userId, String companyName, String email) {
        validate(companyName, email);
        int rows = customerRepository.update(customerId, userId, companyName.trim(), email.trim().toLowerCase());
        if (rows == 0) throw new NoSuchElementException("Customer not found");
        return new Customer(customerId, userId, companyName.trim(), email.trim().toLowerCase());
    }

    public void delete(int customerId, int userId) {
        int rows = customerRepository.delete(customerId, userId);
        if (rows == 0) throw new NoSuchElementException("Customer not found");
    }

    private void validate(String companyName, String email) {
        if (companyName == null || companyName.isBlank()) {
            throw new IllegalArgumentException("Company name is required");
        }
        if (companyName.length() > 100) {
            throw new IllegalArgumentException("Company name is too long");
        }
        if (email != null && !email.isBlank()) {
            if (email.length() > 100)
                throw new IllegalArgumentException("Email too long");
            if (!email.contains("@") || !email.contains("."))
                throw new IllegalArgumentException("Email looks malformed");
        }
    }
}
