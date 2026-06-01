package com.example.fintrackbackend.repository;

import com.example.fintrackbackend.model.Invoice;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

@Repository
public class InvoiceRepository {

    private final JdbcTemplate jdbc;

    public InvoiceRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public List<Invoice> findByUserId(int userId) {
        String sql = "SELECT i.invoice_id, i.customer_id, i.amount, i.due_date, " +
                "i.status, i.created_at, c.company_name, c.email " +
                "FROM Invoice i JOIN Customer c ON i.customer_id = c.customer_id " +
                "WHERE c.user_id = ? " +
                "ORDER BY i.due_date ASC";
        return jdbc.query(sql, new InvoiceRowMapper(), userId);
    }

    public Optional<Invoice> findByIdForUser(int invoiceId, int userId) {
        String sql = "SELECT i.invoice_id, i.customer_id, i.amount, i.due_date, " +
                "i.status, i.created_at, c.company_name, c.email " +
                "FROM Invoice i JOIN Customer c ON i.customer_id = c.customer_id " +
                "WHERE i.invoice_id = ? AND c.user_id = ?";
        try {
            return Optional.ofNullable(jdbc.queryForObject(sql, new InvoiceRowMapper(), invoiceId, userId));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public int insert(Invoice invoice) {
        String sql = "INSERT INTO Invoice (customer_id, amount, due_date, status) " +
                "VALUES (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, invoice.getCustomerId());
            ps.setBigDecimal(2, invoice.getAmount());
            ps.setDate(3, invoice.getDueDate());
            ps.setString(4, invoice.getStatus());
            return ps;
        }, keyHolder);
        return keyHolder.getKey().intValue();
    }

    public int update(int invoiceId, int userId, BigDecimal amount, Date dueDate, String status) {
        String sql = "UPDATE Invoice i JOIN Customer c ON i.customer_id = c.customer_id " +
                "SET i.amount = ?, i.due_date = ?, i.status = ? " +
                "WHERE i.invoice_id = ? AND c.user_id = ?";
        return jdbc.update(sql, amount, dueDate, status, invoiceId, userId);
    }

    public int delete(int invoiceId, int userId) {
        String sql = "DELETE i FROM Invoice i " +
                "JOIN Customer c ON i.customer_id = c.customer_id " +
                "WHERE i.invoice_id = ? AND c.user_id = ?";
        return jdbc.update(sql, invoiceId, userId);
    }

    public int markPaid(int invoiceId, int userId) {
        String sql = "UPDATE Invoice i JOIN Customer c ON i.custoer_id = c.customer_id " +
                "SET i.status = 'paid' WHERE i.invoice_id = ? AND c.user_id = ?";
        return jdbc.update(sql, invoiceId, userId);
    }
}
