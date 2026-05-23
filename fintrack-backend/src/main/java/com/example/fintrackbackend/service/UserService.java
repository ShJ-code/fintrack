package com.example.fintrackbackend.service;

import com.example.fintrackbackend.model.User;
import com.example.fintrackbackend.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

    public UserService(UserRepository userRepository, PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.encoder = encoder;
    }

    public Optional<User> login(String email, String password) {
        if (email == null || email.isBlank() || password == null) {
            return Optional.empty();
        }
        return userRepository.findByEmail(email.trim().toLowerCase())
            .filter(uwh -> uwh.hash() != null && encoder.matches(password, uwh.hash()))
            .map(UserRepository.UserWithHash::user);
    }

    public User register(String name, String email, String password) {
        validateRegistration(name, email, password);
        String normalizedEmail = email.trim().toLowerCase();
        if (userRepository.existsByEmail(normalizedEmail)) {
            throw new IllegalArgumentException("Email already registered");
        }
        String hash = encoder.encode(password);
        int newId = userRepository.insert(name.trim(), normalizedEmail, hash);
        return new User(newId, name.trim(), normalizedEmail, null);
    }

    private void validateRegistration(String name, String email, String password) {
        if (name == null || name.isBlank())
            throw new IllegalArgumentException("Name is required");
        if (email == null || email.isBlank())
            throw new IllegalArgumentException("Email is required");
        if (password == null || password.length() < 8)
            throw new IllegalArgumentException("Password must be at least 8 characters");
    }
}
