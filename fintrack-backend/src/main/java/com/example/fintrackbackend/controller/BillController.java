package com.example.fintrackbackend.controller;

import com.example.fintrackbackend.model.Bill;
import com.example.fintrackbackend.service.BillService;
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
    public List<Bill> getBills(Integer userId) {
        return billService.getBills(userId);
    }

    @PostMapping("/bills")
    public Bill insertBill(@RequestBody Bill bill) {
        return null;
    }
}
