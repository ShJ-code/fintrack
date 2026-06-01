package com.example.fintrackbackend.repository;

import com.example.fintrackbackend.model.Payment;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Types;
import java.util.List;
import java.util.Optional;

@Repository
public class PaymentRepository {

    private final JdbcTemplate jdbc;
    public PaymentRepository(JdbcTemplate jdbc) { this.jdbc = jdbc; }

    private static final RowMapper<Payment> ROW_MAPPER = (rs, n) -> {
        Payment p = new Payment();
        p.setPaymentId(rs.getInt("payment_id"));
        int billId = rs.getInt("bill_id");
        p.setBillId(rs.wasNull() ? null : billId);
        int invoiceId = rs.getInt("invoice_id");
        p.setInvoiceId(rs.wasNull() ? null : invoiceId);
        p.setAmount(rs.getBigDecimal("amount"));
        p.setMethod(rs.getString("method"));
        p.setStatus(rs.getString("status"));
        p.setExternalRef(rs.getString("external_ref"));
        p.setFailureReason(rs.getString("failure_reason"));
        p.setIdempotencyKey(rs.getString("idempotency_key"));
        p.setCreatedAt(rs.getTimestamp("created_at"));
        return p;
    };

    public Optional<Payment> findByIdempotencyKey(String key) {
        try {
            return Optional.ofNullable(jdbc.queryForObject(
                "SELECT * FROM Payment WHERE idempotency_key = ?", ROW_MAPPER, key));
        } catch (EmptyResultDataAccessException e) { return Optional.empty(); }
    }

    public int insertPending(Payment p) {
        KeyHolder kh = new GeneratedKeyHolder();
        jdbc.update(con -> {
            PreparedStatement ps = con.prepareStatement(
                "INSERT INTO Payment (bill_id, invoice_id, amount, method, status, idempotency_key) " +
                "VALUES (?, ?, ?, ?, 'pending', ?)",
                Statement.RETURN_GENERATED_KEYS);
            if (p.getBillId() == null) ps.setNull(1, Types.INTEGER);
            else ps.setInt(1, p.getBillId());
            if (p.getInvoiceId() == null) ps.setNull(2, Types.INTEGER);
            else ps.setInt(2, p.getInvoiceId());
            ps.setBigDecimal(3, p.getAmount());
            ps.setString(4, p.getMethod());
            ps.setString(5, p.getIdempotencyKey());
            return ps;
        }, kh);
        return kh.getKey().intValue();
    }

    public int markSucceeded(int paymentId, String externalRef) {
        return jdbc.update(
            "UPDATE Payment SET status = 'succeeded', external_ref = ? WHERE payment_id = ? AND status = 'pending'",
            externalRef, paymentId);
    }

    public int markFailed(int paymentId, String reason) {
        return jdbc.update(
            "UPDATE Payment SET status = 'failed', failure_reason = ? WHERE payment_id = ? AND status = 'pending'",
            reason, paymentId);
    }

    public List<Payment> findByUserId(int userId) {
        String sql = "SELECT p.* FROM Payment p " +
                "LEFT JOIN Bill b ON b.bill_id = p.bill_id " +
                "LEFT JOIN Vendor v ON v.vendor_id = b.vendor_id " +
                "LEFT JOIN Invoice i ON i.invoice_id = p.invoice_id " +
                "LEFT JOIN Customer c ON c.customer_id = i.customer_id " +
                "WHERE v.user_id = ? OR c.user_id = ? " +
                "ORDER BY p.created_at DESC";
        return jdbc.query(sql, ROW_MAPPER, userId, userId);
    }
}
