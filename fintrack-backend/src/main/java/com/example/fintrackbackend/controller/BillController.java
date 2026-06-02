package com.example.fintrackbackend.controller;

import com.example.fintrackbackend.model.Bill;
import com.example.fintrackbackend.security.CurrentUserId;
import com.example.fintrackbackend.service.BillService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bills")
public class BillController {

    private final BillService billService;

    public BillController(BillService billService) {
        this.billService = billService;
    }

    @GetMapping
    public List<Bill> list(@CurrentUserId Integer userId) {
        return billService.getBillsForUser(userId);
    }

    @PostMapping
    public ResponseEntity<Bill> createBill(
            @CurrentUserId Integer userId,
            @RequestBody Bill bill) {
        return ResponseEntity.status(201).body(billService.createBill(userId, bill));
    }

    @PutMapping("/{billId}")
    public Bill update(@CurrentUserId Integer userId,
                       @PathVariable int billId,
                       @RequestBody Bill bill) {
        return billService.updateBill(billId, userId, bill);
    }

    @DeleteMapping("/{billId}")
    public ResponseEntity<Void> delete(@CurrentUserId Integer userId, @PathVariable int billId) {
        billService.deleteBill(billId, userId);
        return ResponseEntity.noContent().build();
    }
}
