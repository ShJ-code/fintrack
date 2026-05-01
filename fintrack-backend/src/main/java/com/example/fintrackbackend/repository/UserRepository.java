package com.example.fintrackbackend.repository;
import com.example.fintrackbackend.model.User;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserRepository {

    private final JdbcTemplate jdbc;

    public UserRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public Optional<User> findByEmailAndPassword(String email, String password) {
        String sql = "SELECT user_id, name, email, address FROM users WHERE email = ? AND password = ?";

        try {
            User u = jdbc.queryForObject(sql, new UserRowMapper(), email, password);
            return Optional.ofNullable(u);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
}
