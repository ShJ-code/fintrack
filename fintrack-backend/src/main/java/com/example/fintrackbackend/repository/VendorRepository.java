package com.example.fintrackbackend.repository;

import com.example.fintrackbackend.model.Vendor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

@Repository
public class VendorRepository {

    private final JdbcTemplate jdbc;

    public VendorRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    private static final RowMapper<Vendor> ROW_MAPPER = (rs, n) ->
        new Vendor(rs.getInt("vendor_id"),
                   rs.getInt("user_id"),
                   rs.getString("company_name"));

    public List<Vendor> findByUserId(int userId) {
        return jdbc.query("SELECT vendor_id, user_id, company_name FROM Vendor v WHERE v.user_id = ? ORDER BY company_name", ROW_MAPPER, userId);
    }

    public Optional<Vendor> findByIdForUser(int vendorId, int userId) {
        try {
            Vendor v = jdbc.queryForObject(
                "SELECT vendor_id, user_id, company_name FROM Vendor WHERE vendor_id = ? AND user_id = ?",
                ROW_MAPPER, vendorId, userId);
            return Optional.ofNullable(v);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public int insert(int userId, String companyName) {
        KeyHolder kh = new GeneratedKeyHolder();
        jdbc.update(con -> {
            PreparedStatement ps = con.prepareStatement(
                "INSERT INTO Vendor (user_id, company_name) VALUES (?, ?)",
                Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, userId);
            ps.setString(2, companyName);
            return ps;
        }, kh);
        return kh.getKey().intValue();
    }

    /** Returns rows affected; 0 means the (vendorId, userId) pair didn't match anything. */
    public int update(int vendorId, int userId, String companyName) {
        return jdbc.update(
            "UPDATE Vendor SET company_name = ? WHERE vendor_id = ? AND user_id = ?",
            companyName, vendorId, userId);
    }

    public int delete(int vendorId, int userId) {
        return jdbc.update(
            "DELETE FROM Vendor WHERE vendor_id = ? AND user_id = ?",
            vendorId, userId);
    }
}
