package com.aida.usefy_lite.service;

import com.aida.usefy_lite.dto.ChatRequestDto;
import com.aida.usefy_lite.dto.ChatResponseDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ChatService {

    @Value("${ai.api.key}")
    private String apiKey;

    @Value("${ai.api.url}")
    private String apiUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    public ChatResponseDto sendMessage(ChatRequestDto request) {

        // Build request for your external API (pseudo-example)
        var body = new ExternalAiRequest(request.getMessage());
        var response = restTemplate.postForObject(
                apiUrl,
                body,
                ExternalAiResponse.class
        );
        return new ChatResponseDto(response.getReply());
    }

    // Example internal mapping classes
    static class ExternalAiRequest {
        public String input;
        public ExternalAiRequest(String input) { this.input = input; }
    }

    static class ExternalAiResponse {
        public String reply;
        public String getReply() { return reply; }
    }


}
