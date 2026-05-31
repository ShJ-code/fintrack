package com.example.fintrackbackend.service;

import com.example.fintrackbackend.model.Bill;
import com.example.fintrackbackend.repository.BillRepository;
import com.example.fintrackbackend.repository.VendorRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class BillService {

    private final BillRepository billRepository;
    private final VendorRepository vendorRepository;

    public BillService(BillRepository billRepository, VendorRepository vendorRepository) {
        this.billRepository = billRepository;
        this.vendorRepository = vendorRepository;
    }

    public List<Bill> getBillsForUser(Integer userId) {
        return billRepository.findByUserId(userId);
    }

    public Bill createBill(int userId, Bill bill) {
        if (bill.getAmount() == null
            || bill.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Bill amount must be positive");
        }
        if (bill.getDueDate() == null) {
            throw new IllegalArgumentException("Bill due date is required");
        }

        // Ownership check: the target vendor must belong to this user.
        vendorRepository.findByIdForUser(bill.getVendorId(), userId)
                .orElseThrow(() -> new IllegalArgumentException("Unknown vendor"));

        if (bill.getStatus() == null || bill.getStatus().isBlank()) {
            bill.setStatus("unpaid");
        }

        int newId = billRepository.insert(bill);
        bill.setBillId(newId);
        return bill;
    }

    public Bill updateBill(int billId, int userId, Bill bill) {
        if (bill.getAmount() == null || bill.getAmount().compareTo(BigDecimal.ZERO) <= 0)
            throw new IllegalArgumentException("Bill amount must be positive");
        if (bill.getDueDate() == null)
            throw new IllegalArgumentException("Bill due date is required");

        int rows = billRepository.update(billId, userId, bill.getAmount(), bill.getDueDate(), bill.getStatus());
        if (rows == 0) throw new NoSuchElementException("Bill not found");

        bill.setBillId(billId);
        if (bill.getStatus() == null || bill.getStatus().isBlank()) bill.setStatus("unpaid");
        return bill;
    }

    public void deleteBill(int billId, int userId) {
        int rows = billRepository.delete(billId, userId);
        if (rows == 0) throw new NoSuchElementException("Bill not found");
    }
}
