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
import org.springframework.web.servlet.View;

import java.util.HashMap;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final View error;


    public AuthController(UserService userService, PasswordEncoder passwordEncoder, View error) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.error = error;
    }


    //.....................
    //Registration endpoint
    //.....................
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(
            @Valid @RequestBody RegistrationRequest request) {

        userService.registerUser(request);
        return ResponseEntity.ok("User registered successfully!");
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
