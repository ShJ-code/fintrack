package com.example.fintrackbackend.repository;

import com.example.fintrackbackend.model.Invoice;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class InvoiceRowMapper implements RowMapper<Invoice> {

    @Override
    public Invoice mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Invoice(
            rs.getInt("invoice_id"),
            rs.getInt("customer_id"),
            rs.getBigDecimal("amount"),
            rs.getDate("due_date"),
            rs.getString("status"),
            rs.getTimestamp("created_at"),
            rs.getString("company_name"),
            rs.getString("email")
        );
    }
}
