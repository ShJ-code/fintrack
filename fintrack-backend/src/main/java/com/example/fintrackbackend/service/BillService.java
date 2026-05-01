package com.example.fintrackbackend.service;

import com.example.fintrackbackend.model.Bill;
import com.example.fintrackbackend.repository.BillRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class BillService {

    private final BillRepository billRepository;

    public BillService(BillRepository billRepository) {
        this.billRepository = billRepository;
    }

    public List<Bill> getBillsForUser(Integer userId) {
        return billRepository.findByUserId(userId);
    }

    public Bill createBill(Bill bill) {
        if (bill.getAmount() == null
            || bill.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Bill amount must be positive");
        }
        if (bill.getDueDate() == null) {
            throw new IllegalArgumentException("Bill due date is required");
        }
        if (bill.getStatus() == null || bill.getStatus().isBlank()) {
            bill.setStatus("unpaid");
        }

        int newId = billRepository.insert(bill);
        bill.setBillId(newId);
        return bill;
    }
}
