package com.example.fintrackbackend.service;

import com.example.fintrackbackend.model.Vendor;
import com.example.fintrackbackend.repository.VendorRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VendorService {

    private final VendorRepository vendorRepository;

    public VendorService(VendorRepository vendorRepository) {
        this.vendorRepository = vendorRepository;
    }

    public List<Vendor> getVendors(Integer userId) {
        return userId == null
                ? vendorRepository.findAll()
                : vendorRepository.findByUserId(userId);
    }
}
