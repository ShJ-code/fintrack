package com.example.fintrackbackend.controller;

import com.example.fintrackbackend.model.Vendor;
import com.example.fintrackbackend.service.VendorService;
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
    public List<Vendor> getVendors(@RequestParam(required = false) Integer userId) {
        return vendorService.getVendors(userId);
    }

    // To be added later
    @PostMapping("/vendors")
    public Vendor createVendor(@RequestBody Vendor vendor) {
        return null;
    }
}
