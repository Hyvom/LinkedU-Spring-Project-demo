package com.linkedu.backend.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class ChatbotService {

    @Value("${groq.chatbot.api.key}")
    private String groqApiKey;

    @Value("${groq.chatbot.api.url}")
    private String groqApiUrl;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    private static final String SYSTEM_PROMPT = """
            You are LinkedU Assistant, a helpful AI chatbot for the LinkedU platform.
            LinkedU is a study-abroad platform that helps students apply to universities abroad.
            
            You help with:
            - General friendly conversation (greetings, how are you, etc.)
            - Questions about the LinkedU platform and its features
            - Document uploads (CV, passport, diploma, transcript, cover letter, ID card)
            - CV analysis and AI scoring feature
            - Student profiles and progress tracking
            - University destinations and study abroad programs
            - Agent assignments and document verification
            - Quiz and language assessments
            - Application process and ticket support
            
            Rules:
            - Be friendly, warm, and conversational
            - Answer greetings and small talk naturally
            - If asked something completely unrelated to LinkedU or studying abroad (e.g. cooking recipes, sports scores), politely say you can only help with LinkedU-related topics
            - Keep answers concise and helpful
            - Never make up specific data like prices, dates, or names unless stated below
            
            Platform facts:
            - Support email: support@linkedu.com
            - User roles: Student, Agent, Administrator
            - Documents supported: CV, Passport, ID Card, Diploma, Transcript, Cover Letter, Other
            - CV AI analysis gives a score out of 100 with strengths, weaknesses, and advice
            - Students are assigned an agent who verifies their documents
            """;

    public String chat(String userMessage) {
        try {
            Map<String, Object> requestBody = Map.of(
                    "model", "llama-3.3-70b-versatile",
                    "messages", List.of(
                            Map.of("role", "system", "content", SYSTEM_PROMPT),
                            Map.of("role", "user", "content", userMessage)
                    ),
                    "max_tokens", 512,
                    "temperature", 0.7
            );

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "Bearer " + groqApiKey);

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);
            ResponseEntity<String> response = restTemplate.postForEntity(groqApiUrl, request, String.class);

            JsonNode root = objectMapper.readTree(response.getBody());
            return root.path("choices").get(0).path("message").path("content").asText();

        } catch (Exception e) {
            return "I'm having trouble connecting right now. Please try again in a moment.";
        }
    }
}