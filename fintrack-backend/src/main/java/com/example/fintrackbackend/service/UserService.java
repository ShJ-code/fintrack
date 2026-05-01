package com.example.fintrackbackend.service;
import com.example.fintrackbackend.model.User;
import com.example.fintrackbackend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<User> login(String email, String password) {
        if (email == null || email.isBlank() || password == null) {
            return Optional.empty();
        }
        return userRepository.findByEmailAndPassword(email.trim().toLowerCase(), password);
    }
}
