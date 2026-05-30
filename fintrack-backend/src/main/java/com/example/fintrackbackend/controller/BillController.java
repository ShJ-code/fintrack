package com.example.fintrackbackend.controller;

import com.example.fintrackbackend.model.Bill;
import com.example.fintrackbackend.security.CurrentUserId;
import com.example.fintrackbackend.service.BillService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
public class BillController {

    private final BillService billService;

    public BillController(BillService billService) {
        this.billService = billService;
    }

    @GetMapping("/bills")
    public List<Bill> getBills(@CurrentUserId Integer userId) {
        return billService.getBillsForUser(userId);
    }

    @PostMapping("/bills")
    public ResponseEntity<Bill> createBill(
            @RequestAttribute(name = "userId", required = false) Integer userId,
            @RequestBody Bill bill) {
        if (userId == null) return ResponseEntity.status(401).build();
        return ResponseEntity.ok(billService.createBill(bill));
    }
}
