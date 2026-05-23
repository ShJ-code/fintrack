package com.example.fintrackbackend.repository;

import com.example.fintrackbackend.model.User;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Optional;

@Repository
public class UserRepository {

    private final JdbcTemplate jdbc;

    public UserRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public Optional<UserWithHash> findByEmail(String email) {
        String sql = "SELECT user_id, name, email, address, password_hash FROM users WHERE email = ?";

        try {
            UserWithHash uwh = jdbc.queryForObject(sql, (rs, n) -> {
                User u = new User(
                    rs.getInt("user_id"),
                    rs.getString("name"),
                    rs.getString("Email"),
                    rs.getString("address")
                );
                String hash = rs.getString("password_hash");
                return new UserWithHash(u, hash);
            }, email);
            return Optional.ofNullable(uwh);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public boolean existsByEmail(String email) {
        Integer count = jdbc.queryForObject("SELECT count(*) FROM users WHERE email = ?", Integer.class, email);
        return count != null && count > 0;
    }

    /**
     * Inserts a new user record into the database and returns its generated ID.
     *
     * @param name the user's name
     * @param email the user's email address
     * @param passwordHash the hashed password
     * @return the generated primary key of the new user
     */
    public int insert(String name, String email, String passwordHash) {
        String sql = "INSERT INTO users (name, email, password_hash) VALUES (?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update(con -> {
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, passwordHash);
            return ps;
        }, keyHolder);
        return keyHolder.getKey().intValue();
    }

    public record UserWithHash(User user, String hash) {}
}
