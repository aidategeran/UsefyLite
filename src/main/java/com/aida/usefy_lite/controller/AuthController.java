package com.aida.usefy_lite.controller;

import com.aida.usefy_lite.dto.LoginRequest;
import com.aida.usefy_lite.dto.RegistrationRequest;
import com.aida.usefy_lite.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;


    public AuthController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }


    //.....................
    //Registration endpoint
    //.....................
    @PostMapping("/register")
    public ResponseEntity<String> register (@Valid @RequestBody RegistrationRequest request, BindingResult result) {

        if (request.getUsername() == null || request.getUsername().isBlank() ||
                request.getPassword() == null || request.getPassword().isBlank()) {

            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Username and password cannot be empty");
        }

        try {
            userService.registerUser(request);
            return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }

    }

    //...............
    // Login Endpoint
    //...............
    @PostMapping ("/login")
    public ResponseEntity<String> login (@RequestBody LoginRequest request) {
        var user = userService.findByUsername(request.getUsername());
        if (user == null) {
            return new ResponseEntity<>("User not found", HttpStatus.UNAUTHORIZED);
        }

        boolean valid = passwordEncoder.matches(request.getPassword(), user.getPasswordHash() );
        if (!valid) {
            return new ResponseEntity<>("Invalid credentials", HttpStatus.UNAUTHORIZED);
        }

        return ResponseEntity.ok("Login successful");
    }
}
