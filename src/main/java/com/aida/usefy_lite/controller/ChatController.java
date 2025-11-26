package com.aida.usefy_lite.controller;


import com.aida.usefy_lite.dto.ChatRequestDto;
import com.aida.usefy_lite.dto.ChatResponseDto;
import com.aida.usefy_lite.service.ChatService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping
    public ResponseEntity<ChatResponseDto> chat (@Valid @RequestBody ChatRequestDto request) {
        ChatResponseDto response = chatService.sendMessage(request);
        return ResponseEntity.ok(response);
    }
}
