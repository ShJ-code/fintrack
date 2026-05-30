package com.example.fintrackbackend.controller;

import com.example.fintrackbackend.security.CurrentUserId;
import com.example.fintrackbackend.model.Vendor;
import com.example.fintrackbackend.service.VendorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
public class VendorController {

    private final VendorService vendorService;

    public VendorController(VendorService vendorService) {
        this.vendorService = vendorService;
    }

    @GetMapping("/vendors")
    public List<Vendor> getVendors(@CurrentUserId Integer userId) {
        return vendorService.getVendors(userId);
    }

    // To be added later
    @PostMapping("/vendors")
    public ResponseEntity<Vendor> createVendor(
            @RequestAttribute(name = "userId", required = false) Integer userId,
            @RequestBody Vendor vendor) {
        if (userId == null) return ResponseEntity.status(401).build();
        return null;
    }
}
