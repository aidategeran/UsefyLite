package com.aida.usefy_lite.controller;

import com.aida.usefy_lite.dto.LoginRequest;
import com.aida.usefy_lite.dto.RegistrationRequest;
import com.aida.usefy_lite.model.User;
import com.aida.usefy_lite.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;




@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private PasswordEncoder passwordEncoder;


    // ✅ Register success
    @Test
    void register_ShouldReturn201_WhenUserIsCreated() throws Exception {

        User user = new User("john", "hashed123");
        user.setId(1L);

        when(userService.registerUser(any(RegistrationRequest.class)))
                .thenReturn(user);

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"john\",\"password\":\"1234\"}"))
                .andExpect(status().isCreated())
                .andExpect(content().string("ser registered successfully"));
    }


    // ❌ Register failure (username exists)
    @Test
    void register_ShouldReturn400_WhenUsernameExists() throws Exception {
        when(userService.registerUser(any(RegistrationRequest.class)))
                .thenThrow(new IllegalArgumentException("Username already exists"));

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"john\",\"password\":\"1234\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Username already exists"));
    }



    // ✅ Login success
    @Test
    void login_ShouldReturn200_WhenCredentialsValid() throws Exception {

        User mockUser = new User("john", "hashed123");
        mockUser.setId(1L);

        when(userService.findByUsername("john")).thenReturn(mockUser);
        when(passwordEncoder.matches("1234", "hashed123")).thenReturn(true);

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"john\",\"password\":\"1234\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("Login successful"));
    }

    // ❌ Login failure — wrong password
    @Test
    void login_ShouldReturn401_WhenPasswordInvalid() throws Exception {
        User mockUser = new User( "john", "hashed123");
        mockUser.setId(1L);

        when(userService.findByUsername("john")).thenReturn(mockUser);
        when(passwordEncoder.matches("wrong", "hashed123")).thenReturn(false);

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"john\",\"password\":\"wrong\"}"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Invalid credentials"));
    }

    // ❌ Login failure — user not found
    @Test
    void login_ShouldReturn401_WhenUserNotFound() throws Exception {
        when(userService.findByUsername("unknown")).thenReturn(null);

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"unknown\",\"password\":\"1234\"}"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("User not found"));
    }


}

