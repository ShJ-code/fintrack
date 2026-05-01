package com.example.fintrackbackend.repository;

import com.example.fintrackbackend.model.Bill;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class BillRowMapper implements RowMapper<Bill> {
    @Override
    public Bill mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Bill(
            rs.getInt("bill_id"),
            rs.getInt("vendor_id"),
            rs.getString("company_name"),
            rs.getBigDecimal("amount"),
            rs.getDate("due_date"),
            rs.getString("status"),
            rs.getTimestamp("created_at")
        );
    }
}
