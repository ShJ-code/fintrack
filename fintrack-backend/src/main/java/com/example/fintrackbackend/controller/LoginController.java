package com.example.fintrackbackend.controller;

import com.example.fintrackbackend.dto.LoginRequest;
import com.example.fintrackbackend.dto.LoginResponse;
import com.example.fintrackbackend.model.User;
import com.example.fintrackbackend.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
public class LoginController {

    private final UserService userService;

    public LoginController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest req) {
        Optional<User> user = userService.login(req.getEmail(), req.getPassword());
        if (user.isEmpty()) {
            return ResponseEntity.status(401).build();
        }
        User u = user.get();
        return ResponseEntity.ok(new LoginResponse(u.getId(), u.getUsername(), u.getEmail()));
    }
}
