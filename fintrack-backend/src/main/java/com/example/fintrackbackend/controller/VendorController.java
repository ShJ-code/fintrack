package com.example.fintrackbackend.controller;

import com.example.fintrackbackend.dto.VendorRequest;
import com.example.fintrackbackend.security.CurrentUserId;
import com.example.fintrackbackend.model.Vendor;
import com.example.fintrackbackend.service.VendorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/vendors")
@CrossOrigin(origins = "http://localhost:5173")
public class VendorController {

    private final VendorService vendorService;

    public VendorController(VendorService vendorService) {
        this.vendorService = vendorService;
    }

    @GetMapping
    public List<Vendor> list(@CurrentUserId Integer userId) {
        return vendorService.getVendors(userId);
    }

    // To be added later
    @PostMapping
    public ResponseEntity<Vendor> create(
            @CurrentUserId Integer userId,
            @RequestBody VendorRequest req) {
        Vendor v = vendorService.create(userId, req.getCompanyName());
        return ResponseEntity.status(201).body(v);
    }

    @PutMapping("/{vendorId}")
    public Vendor update(@CurrentUserId Integer userId,
                         @PathVariable int vendorId,
                         @RequestBody VendorRequest req) {
        return vendorService.update(vendorId, userId, req.getCompanyName());
    }

    @DeleteMapping("/{vendorId}")
    public ResponseEntity<Void> delete(@CurrentUserId Integer userId,
                                       @PathVariable int vendorId) {
        vendorService.delete(vendorId, userId);
        return ResponseEntity.noContent().build();
    }
}
