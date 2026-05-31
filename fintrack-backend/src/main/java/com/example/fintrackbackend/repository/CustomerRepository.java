package com.example.fintrackbackend.repository;

import com.example.fintrackbackend.model.Customer;
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
public class CustomerRepository {

    private final JdbcTemplate jdbc;

    public CustomerRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    private static final RowMapper<Customer> ROW_MAPPER = (rs, n) ->
        new Customer(rs.getInt("customer_id"),
                     rs.getInt("user_id"),
                     rs.getString("company_name"),
                     rs.getString("email"));

    public List<Customer> findByUserId(int userId) {
        return jdbc.query("SELECT customer_id, user_id, company_name, email FROM Customer WHERE user_id = ? ORDER BY company_name", ROW_MAPPER, userId);
    }

    public Optional<Customer> findByIdForUser(int customerId, int userId) {
        try {
            Customer c = jdbc.queryForObject("SELECT customer_id, user_id, company_name, email FROM Customer WHERE user_id = ? AND customer_id = ?",
                    ROW_MAPPER, userId, customerId);
            return Optional.ofNullable(c);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public int insert(int userId, String companyName, String email) {
        KeyHolder kh = new GeneratedKeyHolder();
        jdbc.update(con -> {
            PreparedStatement ps = con.prepareStatement(
                "INSERT INTO Customer (user_id, company_name, email) VALUES (?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, userId);
            ps.setString(2, companyName);
            ps.setString(3, email);
            return ps;
        }, kh);
        return kh.getKey().intValue();
    }

    public int update(int customerId, int userId, String companyName, String email) {
        return jdbc.update(
            "UPDATE Customer SET company_name = ?, email = ? WHERE customer_id = ? AND user_id = ?",
            companyName, email, customerId, userId);
    }

    public int delete(int customerId, int userId) {
        return jdbc.update(
            "DELETE FROM Customer WHERE customer_id = ? AND user_id = ?",
            customerId, userId);
    }
}
