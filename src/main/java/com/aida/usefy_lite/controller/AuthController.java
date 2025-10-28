package com.aida.usefy_lite.controller;

import com.aida.usefy_lite.dto.LoginRequest;
import com.aida.usefy_lite.dto.RegistrationRequest;
import com.aida.usefy_lite.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;


    public AuthController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register (@RequestBody RegistrationRequest request) {
        userService.registerUser(request);
        return new ResponseEntity<>("User registered successfully", HttpStatus.CREATED);
    }


    @PostMapping("/login")
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
