package com.example.fintrackbackend.repository;

import com.example.fintrackbackend.model.Bill;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
public class BillRepository {

    private final JdbcTemplate jdbc;

    public BillRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public List<Bill> findByUserId(Integer userId) {
        String sql = "SELECT b.bill_id, b.vendor_id, v.company_name, " +
                "       b.amount, b.due_date, b.status, b.created_at " +
                "FROM Bill b " +
                "JOIN Vendor v ON v.vendor_id = b.vendor_id " +
                "WHERE v.user_id = ? " +
                "ORDER BY b.due_date ASC";
        return jdbc.query(sql, new BillRowMapper(), userId);
    }

    public int insert(Bill bill) {
        String sql = "INSERT INTO Bill (vendor_id, amount, due_date, status) " +
                "VALUES (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, bill.getVendorId());
            ps.setBigDecimal(2, bill.getAmount());
            ps.setDate(3, bill.getDueDate());
            ps.setString(4, bill.getStatus() == null ? "unpaid" : bill.getStatus());
            return ps;
        }, keyHolder);
        return keyHolder.getKey().intValue();
    }
}
