package com.aida.usefy_lite.service;

import com.aida.usefy_lite.dto.RegistrationRequest;
import com.aida.usefy_lite.model.User;
import com.aida.usefy_lite.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User registerUser(RegistrationRequest userData) {
        //Check if username already exists
        if (userRepository.findByUsername(userData.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Username already exists");
        }

        // Hash the password
        String hashedPassword = passwordEncoder.encode(userData.getPassword());

        // Create entity
        User user = new User();
        user.setUsername(userData.getUsername());
        user.setPasswordHash(hashedPassword);

        // Save to DB
        return userRepository.save(user);
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElse(null);
    }
}