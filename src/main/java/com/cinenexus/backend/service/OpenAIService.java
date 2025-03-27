package com.cinenexus.backend.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;

import java.util.*;

@Service
public class OpenAIService {

    @Value("${openai.api.key}")
    private String openAIKey;

    private final String OPENAI_API_URL = "https://api.openai.com/v1/chat/completions";

    public String getMovieRecommendations(List<String> likedMovies) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + openAIKey);

        String prompt = "User likes these movies: " + String.join(", ", likedMovies) +
                ". Suggest 5 similar movies and return them as a numbered list.";

        Map<String, Object> message = new HashMap<>();
        message.put("role", "user");
        message.put("content", prompt);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "gpt-3.5-turbo");
        requestBody.put("messages", List.of(message));
        requestBody.put("max_tokens", 150);

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<Map> response = restTemplate.exchange(OPENAI_API_URL, HttpMethod.POST, requestEntity, Map.class);

        List<Map<String, Object>> choices = (List<Map<String, Object>>) response.getBody().get("choices");
        Map<String, Object> firstChoice = choices.get(0);
        Map<String, Object> messageContent = (Map<String, Object>) firstChoice.get("message");

        return (String) messageContent.get("content");
    }
}
