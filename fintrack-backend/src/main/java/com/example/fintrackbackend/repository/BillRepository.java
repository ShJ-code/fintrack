package com.example.fintrackbackend.repository;

import com.example.fintrackbackend.model.Bill;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class BillRepository {

    private final JdbcTemplate jdbc;

    public BillRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public List<Bill> findByUserId(Integer userId) {
        String sql = "SELECT b.*, v.company_name FROM Bill b JOIN Vendor v ON v.vendor_id = b.vendor_id WHERE user_id = ?";
        return jdbc.query(sql, (rs, n) -> new Bill(
                rs.getInt("bill_id"),
                rs.getInt("vendor_id"),
                rs.getString("company_name"),
                rs.getBigDecimal("amount"),
                rs.getDate("due_date"),
                rs.getString("status"),
                rs.getTimestamp("created_at")
        ), userId);
    }
}
