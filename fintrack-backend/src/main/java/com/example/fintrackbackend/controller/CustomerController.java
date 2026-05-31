package com.example.fintrackbackend.controller;

import com.example.fintrackbackend.dto.CustomerRequest;
import com.example.fintrackbackend.model.Customer;
import com.example.fintrackbackend.security.CurrentUserId;
import com.example.fintrackbackend.service.CustomerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customers")
@CrossOrigin(origins = "http://localhost:5173")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    public List<Customer> list(@CurrentUserId Integer userId) {
        return customerService.getCustomers(userId);
    }

    @PostMapping
    public ResponseEntity<Customer> create(
        @CurrentUserId Integer userId,
        @RequestBody CustomerRequest req) {
        Customer c = customerService.create(userId, req.getCompanyName(), req.getEmail());
        return ResponseEntity.status(201).body(c);
    }

    @PutMapping("/{customerId}")
    public Customer update(@CurrentUserId Integer userId,
                           @PathVariable int customerId,
                           @RequestBody CustomerRequest req) {
        return customerService.update(customerId, userId, req.getCompanyName(), req.getEmail());
    }

    @DeleteMapping("/{customerId}")
    public ResponseEntity<Void> delete(@CurrentUserId Integer userId,
                                       @PathVariable int customerId) {
        customerService.delete(customerId, userId);
        return ResponseEntity.noContent().build();
    }
}
