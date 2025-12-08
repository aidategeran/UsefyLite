package com.aida.usefy_lite.controller;

import com.aida.usefy_lite.dto.ChatRequestDto;
import com.aida.usefy_lite.dto.ChatResponseDto;
import com.aida.usefy_lite.service.ChatService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ChatController.class)
public class ChatControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ChatService chatService;

    @Autowired
    private ObjectMapper objectMapper;

    // -------- TEST 1: UNAUTHORIZED USER --------
    @Test
    void shouldReturn401_WhenNoTokenProvided() throws Exception {
        ChatRequestDto req = new ChatRequestDto("Hello");

        mockMvc.perform(
                post("/api/chat")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req))
        )
                .andExpect(status().isUnauthorized());
    }

    // -------- TEST 2: AUTHORIZED USER, VALID REQUEST --------
    @Test
    @WithMockUser(username = "aida")
    void shouldReturn200_WhenAuthorized() throws Exception {
        ChatRequestDto req = new ChatRequestDto("Hello!");
        ChatResponseDto mockResponse = new ChatResponseDto("AI: Hi!");

        when(chatService.sendMessage(req)).thenReturn(mockResponse);

        mockMvc.perform(
                        post("/api/chat")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(req))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reply").value("AI: Hi!"));
    }


    // -------- TEST 3: AUTHORIZED USER, BAD INPUT --------
    @Test
    @WithMockUser(username = "aida")
    void shouldReturn400_WhenInvalidInput() throws Exception {
        ChatRequestDto req = new ChatRequestDto(""); // invalid message

        mockMvc.perform(
                        post("/api/chat")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(req))
                )
                .andExpect(status().isBadRequest());
    }


}
