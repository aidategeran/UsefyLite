package com.aida.usefy_lite.service;

import com.aida.usefy_lite.dto.RegistrationRequest;
import com.aida.usefy_lite.model.User;
import com.aida.usefy_lite.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

    }

    @Test
    void registerUser_ShouldSaveUser_WhenUsernameDoesNotExist() {

        // 1️⃣ Arrange (setup)
        RegistrationRequest request = new RegistrationRequest("john", "1234");

        // Mock: user does NOT exist in DB
        when(userRepository.findByUsername("john")).thenReturn(Optional.empty());

        // Mock: hash password without real BCrypt
        when(passwordEncoder.encode("1234")).thenReturn("hashed123");

        // Mock: saving returns same user object
        when(userRepository.save(any(User.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // 2️⃣ Act (call the real service)
        User saved = userService.registerUser(request);

        // 3️⃣ Assert (verify result)
        assertNotNull(saved);
        assertEquals("john", saved.getUsername());
        assertEquals("hashed123", saved.getPasswordHash());
        verify(userRepository, times(1)).save(any(User.class));

    }

    // ✅ registerUser - throw if exists
    @Test
    void registerUser_ShouldThrow_WhenUsernameExists() {
        RegistrationRequest request = new RegistrationRequest("john", "1234");

        when(userRepository.findByUsername("john"))
                .thenReturn(Optional.of(new User()));

        assertThrows(RuntimeException.class,
                () -> userService.registerUser(request));

        verify(userRepository, never()).save(any());
    }

    // ✅ findByUsername - found
    @Test
    void findByUsername_ShouldReturnUser_WhenExists() {
        User user = new User();
        user.setUsername("john");

        when(userRepository.findByUsername("john"))
                .thenReturn(Optional.of(user));

        User result = userService.findByUsername("john");

        assertNotNull(result);
        assertEquals("john", result.getUsername());
    }

    // ✅ findByUsername - not found
    @Test
    void findByUsername_ShouldReturnNull_WhenNotExists() {
        when(userRepository.findByUsername("nope"))
                .thenReturn(Optional.empty());

        User result = userService.findByUsername("nope");

        assertNull(result);
    }
}

