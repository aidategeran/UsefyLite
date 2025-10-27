package com.aida.usefy_lite.controller;

import com.aida.usefy_lite.dto.LoginRequest;
import com.aida.usefy_lite.dto.RegistrationRequest;
import com.aida.usefy_lite.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
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

    public ResponseEntity<String> register (@RequestBody RegistrationRequest request) {
        userService.registerUser();
        return new ResponseEntity<>("User registered successfully", HttpStatus.CREATED);
    }

    public ResponseEntity<String> login (@RequestBody LoginRequest request) {

        return new ResponseEntity<>("User not found", HttpStatus.UNAUTHORIZED);
    }
}
