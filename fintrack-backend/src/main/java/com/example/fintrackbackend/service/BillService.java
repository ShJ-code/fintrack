package com.example.fintrackbackend.service;

import com.example.fintrackbackend.model.Bill;
import com.example.fintrackbackend.repository.BillRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BillService {

    private final BillRepository billRepository;

    public BillService(BillRepository billRepository) {
        this.billRepository = billRepository;
    }

    public List<Bill> getBills(Integer userId) {
        return billRepository.findByUserId(userId);
    }
}
