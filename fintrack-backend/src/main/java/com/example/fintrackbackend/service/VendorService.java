package com.example.fintrackbackend.service;

import com.example.fintrackbackend.model.Vendor;
import com.example.fintrackbackend.repository.VendorRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class VendorService {

    private final VendorRepository vendorRepository;

    public VendorService(VendorRepository vendorRepository) {
        this.vendorRepository = vendorRepository;
    }

    public List<Vendor> getVendors(Integer userId) {
        return vendorRepository.findByUserId(userId);
    }

    public Vendor create(int userId, String companyName) {
        validateName(companyName);
        int newId = vendorRepository.insert(userId, companyName.trim());
        return new Vendor(newId, userId, companyName.trim());
    }

    public Vendor update(int vendorId, int userId, String companyName) {
        validateName(companyName);
        int rows = vendorRepository.update(vendorId, userId, companyName.trim());
        if (rows == 0) throw new NoSuchElementException("Vendor not found");
        return new Vendor(vendorId, userId, companyName.trim());
    }

    public void delete(int vendorId, int userId) {
        int rows = vendorRepository.delete(vendorId, userId);
        if (rows == 0) throw new NoSuchElementException("Vendor not found");
    }

    private void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Company name is required");
        }
        if (name.length() > 100) {
            throw new IllegalArgumentException("Company name is too long");
        }
    }
}
