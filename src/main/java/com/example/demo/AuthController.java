package com.example.demo;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Map;

@RestController
public class AuthController {

    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UserRepository userRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(
            @RequestParam String username,
            @RequestParam String password
    ) {

        if (username == null || password == null) {
            return ResponseEntity.badRequest().body("username and password required");
        }
        if (userRepo.findByUsername(username).isPresent()) {
            return ResponseEntity.badRequest().body("username taken");
        }

        User user = new User(username, passwordEncoder.encode(password));
        userRepo.save(user);
        return ResponseEntity.ok(Map.of("message", "registered"));
    }
}
