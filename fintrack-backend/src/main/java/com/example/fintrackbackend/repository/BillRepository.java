package com.example.fintrackbackend.repository;

import com.example.fintrackbackend.model.Bill;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

@Repository
public class BillRepository {

    private final JdbcTemplate jdbc;

    public BillRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public List<Bill> findByUserId(int userId) {
        String sql = "SELECT b.bill_id, b.vendor_id, v.company_name, " +
                "b.amount, b.due_date, b.status, b.created_at " +
                "FROM Bill b " +
                "JOIN Vendor v ON v.vendor_id = b.vendor_id " +
                "WHERE v.user_id = ? " +
                "ORDER BY b.due_date ASC";
        return jdbc.query(sql, new BillRowMapper(), userId);
    }

    public Optional<Bill> findByIdForUser(int billId, int userId) {
        String sql = "SELECT b.bill_id, b.vendor_id, v.company_name, b.amount, " +
                "b.due_date, b.status, b.created_at " +
                "FROM Bill b JOIN Vendor v ON v.vendor_id = b.vendor_id " +
                "WHERE b.bill_id = ? AND v.user_id = ?";
        try {
            return Optional.ofNullable(jdbc.queryForObject(sql, new BillRowMapper(), billId, userId));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
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

    public int update(int billId, int userId, BigDecimal amount, java.sql.Date dueDate, String status) {
        String sql = "UPDATE Bill b " +
                "JOIN Vendor v ON b.vendor_id = v.vendor_id " +
                "SET b.amount = ?, b.due_date = ?, b.status = ? " +
                "WHERE b.bill_id = ? AND v.user_id = ?";
        return jdbc.update(sql, amount, dueDate, status, billId, userId);
    }

    public int delete(int billId, int userId) {
        String sql = "DELETE b FROM Bill b " +
                "JOIN Vendor v ON v.vendor_id = b.vendor_id " +
                "WHERE b.bill_id = ? AND v.user_id = ?";
        return jdbc.update(sql, billId, userId);
    }

    public int markPaid(int billId, int userId) {
        String sql = "UPDATE Bill b JOIN Vendor v ON b.vendor_id = v.vendor_id " +
                "SET b.status = 'paid' WHERE b.bill_id = ? AND v.user_id = ?";
        return jdbc.update(sql, billId, userId);
    }
}
