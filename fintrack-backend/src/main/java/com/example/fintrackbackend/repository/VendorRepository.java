package com.example.fintrackbackend.repository;

import com.example.fintrackbackend.model.Vendor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class VendorRepository {

    private final JdbcTemplate jdbc;

    public VendorRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public List<Vendor> findAll() {
        return jdbc.query("SELECT vendor_id, user_id, company_name FROM Vendor",
                (rs, n) -> new Vendor(rs.getInt("vendor_id"),
                        rs.getInt("user_id"),
                        rs.getString("company_name")));
    }

    public List<Vendor> findByUserId(int userId) {
        String sql = "SELECT * FROM Vendor v WHERE v.user_id = ?";
        return jdbc.query(sql, (rs, n) -> new Vendor(
                rs.getInt("vendor_id"),
                rs.getInt("user_id"),
                rs.getString("company_name")
        ), userId);
    }
}
