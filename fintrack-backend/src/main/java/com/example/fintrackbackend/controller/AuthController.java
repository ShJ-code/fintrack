package com.example.fintrackbackend.controller;

import com.example.fintrackbackend.dto.LoginRequest;
import com.example.fintrackbackend.dto.LoginResponse;
import com.example.fintrackbackend.dto.RegisterRequest;
import com.example.fintrackbackend.model.User;
import com.example.fintrackbackend.security.JwtService;
import com.example.fintrackbackend.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {

    private final UserService userService;
    private final JwtService jwtService;

    public AuthController(UserService userService, JwtService jwtService) {
        this.userService = userService;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest req) {
        Optional<User> user = userService.login(req.getEmail(), req.getPassword());
        if (user.isEmpty()) {
            return ResponseEntity.status(401).build();
        }
        User u = user.get();
        String token = jwtService.issueFor(u.getId());
        return ResponseEntity.ok(new LoginResponse(u.getId(), u.getUsername(), u.getEmail(), token));
    }

    @PostMapping("/register")
    public ResponseEntity<LoginResponse> register(@RequestBody RegisterRequest req) {
        User u = userService.register(req.getName(), req.getEmail(), req.getPassword());
        String token = jwtService.issueFor(u.getId());
        return ResponseEntity.status(201)
                .body(new LoginResponse(u.getId(), u.getUsername(), u.getEmail(), token));
    }
}
