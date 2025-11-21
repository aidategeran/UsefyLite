package com.aida.usefy_lite.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

@Service
public class AiService {

    @Value("${external.api.key}")
    private String apiKey;

    public String callExternalApi(String input) {
        // Example usage
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + apiKey);

        // ...
        return "response";
    }
}
